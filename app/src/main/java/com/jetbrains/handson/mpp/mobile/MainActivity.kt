package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class MainActivity : AppCompatActivity(), ApplicationContract.View {
    lateinit private var autoAdapter: ArrayAdapter<String>
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
}
