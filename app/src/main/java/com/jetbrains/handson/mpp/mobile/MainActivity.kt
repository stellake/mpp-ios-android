package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ApplicationContract.View {
    lateinit var departureStationSelected: Spinner
    lateinit var arrivalStationSelected: Spinner

    lateinit var selectedDeparture: String
    lateinit var selectedArrival: String

    lateinit var departureCode: String
    lateinit var arrivalCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val presenter = ApplicationPresenter()
        presenter.onViewTaken(this)

        departureStationSelected = findViewById<Spinner>(R.id.station_names_spinner_departure) as Spinner
        arrivalStationSelected = findViewById<Spinner>(R.id.station_names_spinner_arrival) as Spinner

        if (departureStationSelected != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                station_names_spinner_departure
            )
            departureStationSelected.adapter = adapter

            departureStationSelected.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        departureCode =
                            station_names_spinner_departure.split(" ")[-1].replace("[", "")
                                .replace("]", "")
                    }
                }
        }

        if (arrivalStationSelected != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                station_names_spinner_arrival
            )
            arrivalStationSelected.adapter = adapter

            arrivalStationSelected.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        arrivalCode =
                            station_names_spinner_departure.split(" ")[-1].replace("[", "")
                                .replace("]", "")
                    }
                }
        }
    }

    override fun setLabel(text: String) {
//        findViewById<TextView>(R.id.main_text).text = text
    }

    fun onSubmitButtonTapped(view: View) {
//        departureStationSelected = station_names_spinner_departure.selectedItem.toString()
//        arrivalStationSelected  = station_names_spinner_arrival.selectedItem.toString()
//
//        departureCode = departureStationSelected.split(" ")[-1].replace("[", "").replace("]", "")
//        arrivalCode = arrivalStationSelected.split(" ")[-1].replace("[", "").replace("]", "")

        val url = "https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/$departureCode/$arrivalCode/#LiveDepResults"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }


}
