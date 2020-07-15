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

class JourneyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpTable()
    }


    var DataList = listOf("Interns", "LNER", "Mike", "CleanCode")


    private fun setUpTable(){
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        val adapter = MyRecyclerViewAdapter()
        adapter.updateData(DataList)



        RecyclerViewTable.apply{
            this.layoutManager = layoutManager
            this.adapter = adapter
        }
    }
}

class MyRecyclerViewAdapter: RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>(){ //TODO: Move to another file


    private var myData = emptyList<String>()

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bindData(text:String){
            itemView.recyclerView.text = text
            itemView.myButton.text = "Button"
            itemView.myButton.setOnClickListener{
                //Do something when button pressed
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view,parent,false)
        return MyViewHolder(view) //this returns the same cell always, change to make cells look different
    }

    override fun getItemCount(): Int {
        return myData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(myData[position])
    }

    fun updateData(data:List<String>){
        myData = data
        notifyDataSetChanged() //updates the recycler
    }
}
