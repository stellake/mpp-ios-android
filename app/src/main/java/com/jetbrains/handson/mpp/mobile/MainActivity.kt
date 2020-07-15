package com.jetbrains.handson.mpp.mobile

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ApplicationContract.View,

    AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val presenter = ApplicationPresenter()
        presenter.onViewTaken(this)

        val outboundSpinner: Spinner = findViewById(outbound_spinner_control.id)
        ArrayAdapter.createFromResource(
            this,
            R.array.stations_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            outboundSpinner.adapter = adapter
        }

        val inboundSpinner: Spinner = findViewById(inbound_spinner_control.id)
        ArrayAdapter.createFromResource(
            this,
            R.array.stations_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            inboundSpinner.adapter = adapter
        }

        val button: Button = findViewById(station_button.id)
        button.setOnClickListener {
            val origin = outboundSpinner.selectedItem.toString()
            val destination = inboundSpinner.selectedItem.toString()
            presenter.onButtonPressed(origin, destination)
        }

        val journeysRecyclerView = findViewById<RecyclerView>(R.id.journeys_recycler_view)

        journeysRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val journeys = ArrayList<Journey>()

        //adding some dummy data to the list
        journeys.add(Journey("Priyanka Patel", "18122000"))
        journeys.add(Journey("Elliot Barnes", "unknown"))
        journeys.add(Journey("Zachary Freed", "unknown"))

        //creating our adapter
        val adapter = CustomAdapter(journeys)

        //now adding the adapter to recyclerview
        journeysRecyclerView.adapter = adapter
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, arg1: View?, position: Int, id: Long) {
        parent?.getItemAtPosition(position)

    }

    override fun setLabel(text: String) {
        findViewById<TextView>(R.id.main_text).text = text
    }

    override fun showData(text: FaresResponse) {
    }

    override fun showAlert(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }


}
