package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable


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

    override fun displayFares(fareList: List<List<String>>) {
        val intent = Intent(this, JourneyActivity::class.java).apply{
            putExtra("fareList",  fareList as Serializable)
        }
        startActivity(intent)
    }

    override fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setPositiveButton("OK",null)


        builder.show()


    }

    fun getJourneyButtonClick(view: View) {
        val departureName = departure_station_spinner.selectedItem.toString()
        val destinationName = destination_station_spinner.selectedItem.toString()
        presenter.loadJourneys(departureName, destinationName)
    }
}