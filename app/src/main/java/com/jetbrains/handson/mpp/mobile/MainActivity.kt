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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val presenter = ApplicationPresenter()
        presenter.onViewTaken(this)

        departureStationSelected = findViewById<Spinner>(R.id.station_names_spinner_departure) as Spinner
        arrivalStationSelected = findViewById<Spinner>(R.id.station_names_spinner_arrival) as Spinner
        departureStationSelected.setSelection(0)
        arrivalStationSelected.setSelection(1)

    }

    override fun setLabel(text: String) {
//        findViewById<TextView>(R.id.main_text).text = text
    }

    fun onSubmitButtonTapped(view: View) {
        val departureCode : String = departureStationSelected.selectedItem.toString().split(" ").last().replace("[", "").replace("]", "");
        val arrivalCode : String = arrivalStationSelected.selectedItem.toString().split(" ").last().replace("[", "").replace("]", "");


        val url = "https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/$departureCode/$arrivalCode/#LiveDepResults"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}
