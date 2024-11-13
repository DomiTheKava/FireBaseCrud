package com.example.firebasecrud.main

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.example.firebasecrud.Person
import com.example.firebasecrud.R
import com.example.firebasecrud.SwipeCallBack
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var rv: RecyclerView

    private lateinit var database: DatabaseReference

    // callback object for when an object in recyclerView is swipes
    val swipeDeleteCallback = object: SwipeCallBack() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.adapterPosition

            Log.i("test", viewModel.users[pos].id)

            // remove from DB
            viewModel.deleteUserFromDatabase(viewModel.users[pos].id, this@MainActivity)

        }
    }

    // update info when database changes
    val changeListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.hasChildren()) {
                viewModel.users.clear()
                for (child in snapshot.children) {

                    // store data in ViewModel
                    val holdData = child.getValue(Person::class.java) // not adding it correctly
                    viewModel.users.add(holdData!!)

                    Log.i("test", child.getValue().toString())
                }

                // update and set recyclerView || Im sure there is a better way
                val adapter = RVAdapter(viewModel.users)
                rv = findViewById(R.id.recylerView)
                rv.adapter = adapter
                rv.layoutManager = LinearLayoutManager(this@MainActivity)

                val itemTouchHelper = ItemTouchHelper(swipeDeleteCallback)
                itemTouchHelper.attachToRecyclerView(rv)

            }
        }

        override fun onCancelled(error: DatabaseError) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        database = Firebase.database.getReference("/users")

        database.addValueEventListener(changeListener)

        // dummy data
//        database.child("005").child("id").setValue("005")
//        database.child("005").removeValue()

        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {// Fab Dialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Add Data")

            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            val nameET = EditText(this)
            nameET.hint = "Name"
            val ageET = EditText(this)
            ageET.hint = "Age"
            ageET.inputType = InputType.TYPE_CLASS_NUMBER
            val numberET = EditText(this)
            numberET.hint = "Contact Info"

            layout.addView(nameET)
            layout.addView(ageET)
            layout.addView(numberET)

            builder.setView(layout)

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            builder.setPositiveButton("ok") { dialog, _ ->
                // nothin, just created this to reference and customize later :)
            }

            val dialog = builder.create()
            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    if (nameET.text.isEmpty() || ageET.text.isEmpty() || numberET.text.isEmpty()) {
                        Toast.makeText(this@MainActivity, "Fill all fields.", Toast.LENGTH_LONG).show()
                    } else {
                        val id = (viewModel.users.last().id.toInt() + 1).toString()
                        val name = nameET.text.toString()
                        val age = ageET.text.toString().toInt()
                        val contact = numberET.text.toString()

                        // add to DB
                        viewModel.addUserToDatabase(name, age, contact, id)

                        dialog.dismiss()
                    }
                }

            }

            dialog.show()

        } // fab Dialog builder

    }

    override fun onDestroy() {
        super.onDestroy()
        database.removeEventListener(changeListener) // prevent data leaks
    }

}