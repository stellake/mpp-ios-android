package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), ApplicationContract.View {
    private lateinit var autoAdapter: ArrayAdapter<String>
    val autoValidator=AutoValidator()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val presenter = ApplicationPresenter()
        presenter.onViewTaken(this)
        setupAutos()
        setupButton(presenter)
    }

    private fun setupAutos() {
        autoAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mutableListOf())
        val autos = listOf<AutoCompleteTextView>(
            findViewById(R.id.arrival_station),
            findViewById(R.id.departure_station)
        )
        autos.forEach {

            it.setAdapter(autoAdapter)
            it.validator = autoValidator
        }
    }

    private fun setupButton(presenter: ApplicationPresenter) {
        val button: Button = findViewById(R.id.done_button)
        button.setOnClickListener {
            val departureArrival=getDepartureArrivalStations()
            if (departureArrival.first!="" && departureArrival.second!="") {
                presenter.onStationsSubmitted(departureArrival.first, departureArrival.second)
            }
        }
    }


    override fun openURL(url: String) {
        startActivity(Intent(ACTION_VIEW, Uri.parse(url)))
    }

    private fun getDepartureArrivalStations(): Pair<String, String> {
        val arrivalText: AutoCompleteTextView = findViewById(R.id.arrival_station)
        arrivalText.performValidation()
        val departureText: AutoCompleteTextView = findViewById(R.id.departure_station)
        departureText.performValidation()
        return Pair(
            departureText.text.toString(),
            arrivalText.text.toString()
        )
    }

    override fun showData(data: List<ApplicationContract.TrainJourney>) {
        println(data)
    }

    override fun updateStations(data: List<String>) {
        autoAdapter.clear()
        autoAdapter.addAll(data)
        autoValidator.valid_list=data
    }

    override fun setLabel(text: String) {
        findViewById<TextView>(R.id.main_text).text = text
    }

    override fun showAPIError(info:String) {
        //TODO - bring up error message
        println("API CALL FAILED")
        println(info)
    }
}
