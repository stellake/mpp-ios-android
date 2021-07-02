/*
package com.jetbrains.handson.mpp.mobile

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val times: ArrayList<OutboundJourney>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_item_row, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return times.size
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        var departureTime: TextView
        var arrivalTime: TextView

        init {
            departureTime = itemView.findViewById(R.id.departure_time)
            arrivalTime = itemView.findViewById(R.id.arrival_time)
        }
    }
}*/

package com.jetbrains.handson.mpp.mobile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(context: Context, journeys: List<OutboundJourney>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mData: List<OutboundJourney> = journeys
    private var mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = mInflater.inflate(R.layout.recyclerview_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val outboundJourney: OutboundJourney = mData[position]
        if (holder is ViewHolder) {
            holder.departureTime.text = outboundJourney.departureTime
            holder.arrivalTime.text = outboundJourney.arrivalTime
        }
    }

    override fun getItemCount(): Int {
        return this.mData.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var departureTime: TextView = view.findViewById(R.id.departure_time)
        var arrivalTime: TextView = view.findViewById(R.id.arrival_time)
    }


}