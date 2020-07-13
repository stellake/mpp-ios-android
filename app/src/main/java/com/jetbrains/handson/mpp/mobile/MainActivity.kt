package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView

class MainActivity : AppCompatActivity(), ApplicationContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val presenter = ApplicationPresenter()
        presenter.onViewTaken(this)
        val spinners= listOf<Spinner>(findViewById(R.id.arrival_station),findViewById(R.id.departure_station))
// Create an ArrayAdapter using the string array and a default spinner layout
        spinners.forEach {
            ArrayAdapter.createFromResource(
                this,
                R.array.stations_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                it.adapter = adapter
            }
        }
        val button:Button=findViewById(R.id.done_button)
        button.setOnClickListener {
            presenter.onDoneButtonPressed()
        }
    }
    override fun openURL(url:String){
        startActivity(Intent(ACTION_VIEW,Uri.parse(url)))
    }
    override fun getArrivalDepartureStations():Pair<String,String>{
        val arrivalSpinner:Spinner=findViewById(R.id.arrival_station)
        val departureSpinner:Spinner=findViewById(R.id.departure_station)
        return Pair(arrivalSpinner.selectedItem.toString(),departureSpinner.selectedItem.toString())
    }
    override fun setLabel(text: String) {
        findViewById<TextView>(R.id.main_text).text = text
    }
}
