package com.jetbrains.handson.mpp.mobile

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.get

fun openNewTabWindow(urls: String, context: Context) {
    val uris = Uri.parse(urls)
    val intents = Intent(Intent.ACTION_VIEW, uris)
    val b = Bundle()
    b.putBoolean("new_window", true)
    intents.putExtras(b)
    context.startActivity(intents)
}


class MainActivity : AppCompatActivity(), ApplicationContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val presenter = ApplicationPresenter()
        presenter.onViewTaken(this)

        val departingSpinner: Spinner = findViewById(R.id.departing_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.departing_stations_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            departingSpinner.adapter = adapter
        }

        val arrivalSpinner: Spinner = findViewById(R.id.arrival_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.departing_stations_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            arrivalSpinner.adapter = adapter
        }
        val button: Button = findViewById(R.id.button_id)
        button.setOnClickListener {
            var departingCode:String = ""
            var arrivalCode = ""

            var stationList = resources.getStringArray(R.array.departing_stations_array)
            var stationCodes = resources.getStringArray(R.array.departing_stations_codes)

            for( i in stationList.indices)
            {
                if(departingSpinner.selectedItem == stationList[i])
                    departingCode = stationCodes[i]

                if(arrivalSpinner.selectedItem == stationList[i])
                   arrivalCode = stationCodes[i]
            }

            openNewTabWindow("https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/".plus(departingCode).plus("/").plus(arrivalCode).plus("/#LiveDepResults"), this@MainActivity)
        }
    }
    override fun setLabel(text: String) {
        findViewById<TextView>(R.id.main_text).text = text
    }
}
