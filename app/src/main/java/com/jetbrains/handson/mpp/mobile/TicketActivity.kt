package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.journey_view.*
import kotlinx.android.synthetic.main.ticket_view.*
import kotlinx.android.synthetic.main.ticket_recycler_view.view.*
import kotlinx.android.synthetic.main.ticket_view.FromTo

class TicketActivity : AppCompatActivity() {


    var tickets = listOf(TicketOption())
    var destinationStation: String = ""
    var originStation: String = ""
    private val tableAdapter = TicketRecycler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ticket_view)
        setUpTable()

        val journey = Gson().fromJson(intent.getSerializableExtra("journey") as String, JourneyOption::class.java)
        tickets = journey.tickets
        destinationStation = journey.destinationStation.displayName
        originStation = journey.originStation.displayName

        Log.e("msg", tickets[0].toString())
        FromTo.text = originStation + " - " + destinationStation
        //FromTo.text =
            //fares.outboundJourneys[0].originStation.displayName + " - " + fares.outboundJourneys[0].destinationStation.displayName
        tableAdapter.updateData(tickets)
    }




    private fun setUpTable() {

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        tableAdapter.updateData(tickets)

        ticketViewTable.apply {
            this.layoutManager = layoutManager
            this.adapter = tableAdapter
        }
    }

}

class TicketRecycler: RecyclerView.Adapter<TicketRecycler.MyViewHolder>(){

    private var tickets = listOf(TicketOption())

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){

        fun bindData(name:String, numberoftickets:Int, priceInPennies:Int){
            Log.e("msg", name)

            val price = priceInPennies.toString()
            val pounds = price.substring(0,price.length-2)
            val pennies = price.substring(price.length-2,price.length)
            val priceAsString = "from £$pounds.$pennies"

            itemView.nameView.text = name
            itemView.numberView.text = if (numberoftickets == 1) "1 passenger" else numberoftickets.toString() + " passengers"
            itemView.priceView.text = priceAsString
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ticket_recycler_view,parent,false)
        return MyViewHolder(view) //this returns the same cell always, change to make cells look different
    }

    override fun getItemCount(): Int {
        Log.e("msg", tickets.size.toString())

        return tickets.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Log.e("msg", tickets[position].name)

        holder.bindData(tickets[position].name, tickets[position].numberOfTickets, tickets[position].priceInPennies)
    }

    fun updateData(tickets: List<TicketOption>){
        this.tickets = tickets
        notifyDataSetChanged() //updates the recycler
    }

}
