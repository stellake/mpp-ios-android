package com.jetbrains.handson.mpp.mobile

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class CustomAdapter(private val journeyList: ArrayList<Journey>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.journeys_list_layout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        holder.bindItems(journeyList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return journeyList.size
    }

    //the class is holding the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: Journey) {
            val textViewName = itemView.findViewById(R.id.name) as TextView
            val textViewAddress  = itemView.findViewById(R.id.dob) as TextView
            textViewName.text = user.name
            textViewAddress.text = user.dob
        }
    }
}