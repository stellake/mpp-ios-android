package com.jetbrains.handson.mpp.mobile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.journey_view.*
import kotlinx.android.synthetic.main.recycler_view.view.*

class JourneyActivity : AppCompatActivity() {

    var DataList: List<List<String>> = listOf(listOf("ignore", "display"))
    val adapter = MyRecyclerViewAdapter()



    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.journey_view)
        setUpTable()
        DataList = intent.getSerializableExtra("fareList") as List<List<String>>
        adapter.updateData(DataList)
    }



    private fun setUpTable(){

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        adapter.updateData(DataList)



        recyclerViewTable.apply{
            this.layoutManager = layoutManager
            this.adapter = adapter
        }
    }
}

class MyRecyclerViewAdapter: RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>(){ //TODO: Move to another file

    private var myData = emptyList<List<String>>()

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
        holder.bindData(myData[position][1])
    }

    fun updateData(data:List<List<String>>){
        myData = data
        notifyDataSetChanged() //updates the recycler
    }
}
