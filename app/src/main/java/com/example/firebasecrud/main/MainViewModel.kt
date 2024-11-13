package com.example.firebasecrud.main

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.firebasecrud.Person
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class MainViewModel(application: Application) : AndroidViewModel(application) {

    public val users: ArrayList<Person>
    lateinit var database: DatabaseReference



init {
    users = ArrayList<Person>()
    database = Firebase.database.getReference("/users")
    val context = getApplication<Application>().applicationContext
}

    fun addUserToDatabase(name: String, age: Int, contact: String, id: String) {
        database.child(id).child("firstName").setValue(name)
        database.child(id).child("id").setValue(id)
        database.child(id).child("number").setValue(contact)
        database.child(id).child("age").setValue(age)
    }

    fun deleteUserFromDatabase(id: String, context: Context) {
        database.child(id).removeValue().addOnSuccessListener {
            Toast.makeText(context, "Successfully deleted from database.", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to delete.", Toast.LENGTH_LONG).show()
        }
    }

}