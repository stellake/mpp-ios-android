package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.journey_view.*
import kotlinx.android.synthetic.main.recycler_view.view.*

class MainActivity : AppCompatActivity(), ApplicationContract.View {


    private val presenter = ApplicationPresenter()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        presenter.onViewTaken(this)
        updateDropDowns(listOf())
    }

    override fun updateDropDowns(stationNames: List<String>) {
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            stationNames
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            departure_station_spinner.adapter = adapter
            destination_station_spinner.adapter = adapter
        }
    }

    override fun setButtonAvailability(state: Boolean) {
        get_journey_button.isEnabled = state
        get_journey_button.isClickable = state
    }

    override fun displayFares(fareList: List<JourneyDetailsLight>) {
        // TODO: this
    }

    override fun showAlert(message: String) {
        // TODO: this
    }

    fun getJourneyButtonClick(view: View) {
        val departureName = departure_station_spinner.selectedItem.toString()
        val destinationName = destination_station_spinner.selectedItem.toString()
        presenter.loadJourneys(this, departureName, destinationName)
        val intent = Intent(this, JourneyActivity::class.java)
        startActivity(intent)
    }
}
