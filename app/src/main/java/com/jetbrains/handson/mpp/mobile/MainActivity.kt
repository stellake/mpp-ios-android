package com.jetbrains.handson.mpp.mobile

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), ApplicationContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val presenter = ApplicationPresenter()
        presenter.onViewTaken(this)

        // Find departure and arrival spinners from XML
        val spinnerDep: Spinner = findViewById(R.id.departure_station)
        val spinnerArr: Spinner = findViewById(R.id.arrival_station)

        // Create an ArrayAdapter using the string array and the custom text formatting
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            applicationContext,
            R.layout.spinner_item,
            arrayOf("Station1", "Station2", "Station3", "Station4", "Station5")
        )

        // Set the drop down view to use a default spinner item on top of the custom text format
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDep.adapter = adapter
        spinnerArr.adapter = adapter
    }

    override fun setLabel(text: String) {
        findViewById<TextView>(R.id.main_text).text = text
    }
}
