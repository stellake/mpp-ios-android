package com.jetbrains.handson.mpp.mobile

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.journeys_list_layout.view.*

class JourneyAdapter(private val journeyList: ArrayList<Journey>) : RecyclerView.Adapter<JourneyAdapter.MyViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JourneyAdapter.MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.journeys_list_layout, parent, false)
        return MyViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: JourneyAdapter.MyViewHolder, position: Int) {
        holder.bindItems(journeyList[position].depTime, journeyList[position].arrTime, journeyList[position].duration) { journeyList[position].button }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return journeyList.size
    }

    //the class is holding the list view
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(inbound: String, outbound: String, duration: String, onStationButtonClick: () -> Unit) {
            itemView.departureTime.text = inbound
            itemView.arrivalTime.text = outbound
            itemView.duration.text = duration
            itemView.go_to_buy.setOnClickListener {
                onStationButtonClick()
            }
        }
    }
}