package com.jetbrains.handson.mpp.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.view.View
import android.content.Intent
import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.net.Uri
import android.widget.SpinnerAdapter
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ApplicationContract.View, AdapterView.OnItemSelectedListener {

    var departure : String = ""
    var arrival : String = ""

    val map = mapOf(
        "York" to "YRK",
        "Kings Cross" to "KGX",
        "Doncaster" to "DON",
        "Peterborough" to "PBO",
        "Grantham" to "GRA"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val presenter = ApplicationPresenter()
        presenter.onViewTaken(this)

        val departure: Spinner = findViewById(R.id.departure_spinner)
        departure.onItemSelectedListener = this

        ArrayAdapter.createFromResource(
            this,
            R.array.departure_spinner,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            departure.adapter = adapter
        }

        val arrival: Spinner = findViewById(R.id.arrival_spinner)
        arrival.onItemSelectedListener = this

        ArrayAdapter.createFromResource(
            this,
            R.array.arrival_spinner,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            arrival.adapter = adapter
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        val selected = parent.getItemAtPosition(pos)
        if(parent.id == R.id.departure_spinner) departure = map[selected]!!
        else arrival = map[selected]!!
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    override fun setLabel(text: String) {
        //findViewById<TextView>(R.id.main_text).text = text
    }

    fun showStations(view: View) {

        val uri = "https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/" + departure + "/" + arrival + "/#LiveDepResults"
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_WEB_SEARCH
            putExtra(SearchManager.QUERY, uri)
        }
        try {
            if (!(departure == arrival)) startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            println(e.message)
        }
    }
}
