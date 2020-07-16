package com.jetbrains.handson.mpp.mobile

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jetbrains.handson.mpp.mobile.api.JourneyOption
import kotlinx.android.synthetic.main.journeys_list_layout.view.*

class JourneyAdapter() : RecyclerView.Adapter<JourneyAdapter.MyViewHolder>() {

    private var journeyList = emptyList<Journey>()
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(inbound: String, outbound: String, duration: String, onBuyButton: () -> Unit) {
            itemView.departureTime.text = inbound
            itemView.arrivalTime.text = outbound
            itemView.duration.text = duration
            itemView.go_to_buy.setOnClickListener {
                onBuyButton()
            }
        }
    }
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.journeys_list_layout, parent, false)
        return MyViewHolder(view)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(journeyList[position].depTime, journeyList[position].arrTime, journeyList[position].duration) { journeyList[position].button }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return journeyList.size
    }

    fun updateData(data: List<Journey>) {
        journeyList = data
        notifyDataSetChanged()
    }
    //the class is holding the list view

}