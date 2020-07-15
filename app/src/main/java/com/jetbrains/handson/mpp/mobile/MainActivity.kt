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


class MainActivity : AppCompatActivity(), ApplicationContract.View {

    fun openNewTabWindow(urls: String, context: Context) {
        val uris = Uri.parse(urls)
        val intents = Intent(Intent.ACTION_VIEW, uris)
        val b = Bundle()
        b.putBoolean("new_window", true)
        intents.putExtras(b)
        context.startActivity(intents)
    }

    fun newLocationsSpinner(context:Context,spinnerID:Int):Spinner
    {
        val theSpinner: Spinner = findViewById(spinnerID)
        ArrayAdapter.createFromResource(
            context,
            R.array.departing_stations_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            theSpinner.adapter = adapter
        }
        return theSpinner
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val presenter = ApplicationPresenter()
        presenter.onViewTaken(this)

        val departingSpinner: Spinner =newLocationsSpinner(this,R.id.departing_spinner)

        val arrivalSpinner : Spinner =newLocationsSpinner(this,R.id.departing_spinner)

        val button: Button = findViewById(R.id.button_id)
        button.setOnClickListener {
            var departingCode:String = ""
            var arrivalCode = ""

            var stationList = resources.getStringArray(R.array.departing_stations_array)
            var stationCodes = resources.getStringArray(R.array.departing_stations_codes)

            for( index in stationList.indices)
            {
                if(departingSpinner.selectedItem == stationList[index])
                    departingCode = stationCodes[index]

                if(arrivalSpinner.selectedItem == stationList[index])
                   arrivalCode = stationCodes[index]
            }

            openNewTabWindow("https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/".plus(departingCode).plus("/").plus(arrivalCode).plus("/#LiveDepResults"), this@MainActivity)
        }
    }
    override fun setLabel(text: String) {
        findViewById<TextView>(R.id.main_text).text = text
    }
}
