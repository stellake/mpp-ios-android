package com.jetbrains.handson.mpp.mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.journey_view.*
import kotlinx.android.synthetic.main.recycler_view.view.*
import com.google.gson.Gson

class JourneyActivity : AppCompatActivity() {

    var fares = Fares()
    private val tableAdapter = MyRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.journey_view)
        setUpTable()
        fares = Gson().fromJson(intent.getSerializableExtra("fareList") as String, Fares::class.java)
        tableAdapter.updateData(fares)
    }

    private fun setUpTable(){

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        tableAdapter.updateData(fares)

        recyclerViewTable.apply{
            this.layoutManager = layoutManager
            this.adapter = tableAdapter
        }
    }
}

class MyRecyclerViewAdapter: RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>(){ //TODO: Move to another file

    private var fares = Fares()

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){

        fun bindData(time: String, price: String){
//            itemView.recyclerView.text = text
//            itemView.myButton.text = "Button"
//            itemView.myButton.setOnClickListener{
//                //Do something when button pressed
//            }

            // TODO: assign text to table cell text or something
            // Just assigned the time for testing
            itemView.priceView.text = time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view,parent,false)
        return MyViewHolder(view) //this returns the same cell always, change to make cells look different
    }

    override fun getItemCount(): Int {
        return fares.outboundJourneys.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val time = fares.outboundJourneys[position].departureTime
        val price = fares.outboundJourneys[position].tickets.map { it.priceInPennies }.min().toString()
        val pounds = price.substring(0,price.length-2)
        val pennies = price.substring(price.length-2,price.length)
        val priceAsString = "from £$pounds.$pennies"
        holder.bindData(time, priceAsString)
    }

    fun updateData(fares: Fares){
        this.fares = fares
        notifyDataSetChanged() //updates the recycler
    }
}
