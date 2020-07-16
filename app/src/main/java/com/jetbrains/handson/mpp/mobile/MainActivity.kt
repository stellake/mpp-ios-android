package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.results_table_cell.view.*

class MainActivity : AppCompatActivity(), ApplicationContract.View {
    lateinit private var autoAdapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val presenter = ApplicationPresenter()
        presenter.onViewTaken(this)
        setupAutos()
        setupButton(presenter)
        setupTable()
    }

    private fun setupAutos() {
        autoAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mutableListOf())
        val autos = listOf<AutoCompleteTextView>(
            findViewById(R.id.arrival_station),
            findViewById(R.id.departure_station)
        )
        autos.forEach {

            it.setAdapter(autoAdapter);
        }
    }

    private fun setupButton(presenter: ApplicationPresenter) {
        val button: Button = findViewById(R.id.done_button)
        button.setOnClickListener {
            presenter.onDoneButtonPressed()
        }
    }


    override fun openURL(url: String) {
        startActivity(Intent(ACTION_VIEW, Uri.parse(url)))
    }

    override fun getArrivalDepartureStations(): Pair<String, String> {
        val arrivalText: AutoCompleteTextView = findViewById(R.id.arrival_station)
        val departureText: AutoCompleteTextView = findViewById(R.id.departure_station)
        return Pair(
            arrivalText.text.toString(),
            departureText.text.toString()
        )
    }

    override fun showData(data: List<ApplicationContract.TrainJourney>) {
        println(data)
    }

    override fun updateStations(data: List<String>) {
        autoAdapter.clear()
        autoAdapter.addAll(data)
    }

    override fun setLabel(text: String) {
        findViewById<TextView>(R.id.main_text).text = text
    }

    //Results Table

    private val data = listOf("Test 1","Test 2","Test 3","Test 4","Test 5")

    private fun setupTable() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = ResultsTableAdapter()
        adapter.updateData(data)

        resultsTable.apply {
            this.layoutManager = layoutManager
            this.adapter = adapter
        }
    }
}

class  ResultsTableAdapter: RecyclerView.Adapter<ResultsTableAdapter.MyViewHolder>() {
    private var myData = emptyList<String>()

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindData(text: String, onButtonClick:() -> Unit) {
            itemView.resultsTableCellText1.text = text
            itemView.myButton.setOnClickListener {
                onButtonClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.results_table_cell, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(
            myData[position]
        ) { println(myData[position])}
    }

    fun updateData(data: List<String>) {
        myData = data
        notifyDataSetChanged()
    }
}
