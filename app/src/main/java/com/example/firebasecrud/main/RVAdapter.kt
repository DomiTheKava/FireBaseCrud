package com.example.firebasecrud.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasecrud.Person
import com.example.firebasecrud.R

class RVAdapter(val users: ArrayList<Person> = ArrayList<Person>()): RecyclerView.Adapter<RVAdapter.ViewHolder>() {

//    private var users = ArrayList<Person>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_custom_row, parent, false))
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = users[position]

        holder.itemView.findViewById<TextView>(R.id.id).text = item.id
        holder.itemView.findViewById<TextView>(R.id.name).text = item.firstName
        holder.itemView.findViewById<TextView>(R.id.age).text = item.age.toString()
        holder.itemView.findViewById<TextView>(R.id.number).text = item.number

    }

}