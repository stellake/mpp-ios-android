package com.jetbrains.handson.mpp.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ApplicationContract.View {

    private val presenter = ApplicationPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.onViewTaken(this)

        updateDropDowns(listOf())
    }

    override fun setLabel(text: String) {
        findViewById<TextView>(R.id.ticket_prices).text = text
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

    fun getJourneyButtonClick(view: View) {
        val departureName = departure_station_spinner.selectedItem.toString()
        val destinationName = destination_station_spinner.selectedItem.toString()
//        val request = presenter.getTimesRequest(departCode, destCode)
//        val intent = Uri.parse(request).let { webpage ->
//            Intent(Intent.ACTION_VIEW, webpage)
//        }
//        startActivity(intent)
        presenter.loadJourneys(this, departureName, destinationName)
    }

    override fun displayFares(fares: Fares) {
        // TODO: This
    }
}
