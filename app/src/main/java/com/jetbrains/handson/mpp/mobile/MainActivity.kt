package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.runBlocking
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ApplicationContract.View {

    lateinit var departureStationSpinner: Spinner
    lateinit var arrivalStationSpinner: Spinner
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var adapter: RecyclerAdapter

//    private lateinit var linearLayoutManager: RecyclerView.LinearLayoutManager

    private val presenter: ApplicationPresenter = ApplicationPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearLayoutManager = LinearLayoutManager(this)
        var recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = linearLayoutManager


        adapter = RecyclerAdapter(this, listOf(OutboundJourney(arrivalTime = "Test", departureTime = "Test"), OutboundJourney(arrivalTime = "Test", departureTime = "Test"),OutboundJourney(arrivalTime = "Test", departureTime = "Test"),OutboundJourney(arrivalTime = "Test", departureTime = "Test")))
        recyclerView.adapter = adapter


        presenter.onViewTaken(this)

        departureStationSpinner = findViewById<Spinner>(R.id.stations_spinner1) as Spinner
        arrivalStationSpinner = findViewById<Spinner>(R.id.stations_spinner2) as Spinner
        departureStationSpinner.setSelection(0)
        arrivalStationSpinner.setSelection(0)

//        linearLayoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = linearLayoutManager
    }

    override fun setLabel(text: String) {
        //findViewById<TextView>(R.id.main_text).text = text
    }

    fun onSubmitClicked(view: View) {

        var departureCode = departureStationSpinner.selectedItem.toString()
        val ld = departureCode.length
        departureCode = departureCode.substring(ld - 4, ld - 1)

        var arrivalCode = arrivalStationSpinner.selectedItem.toString()
        val ad = arrivalCode.length
        arrivalCode = arrivalCode.substring(ad - 4, ad - 1)
        val url =
            "https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/$departureCode/$arrivalCode/#LiveDepResults"

        presenter.getTrainTimes(departureCode, arrivalCode)

//        println("printed msg")

//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.data = Uri.parse(url)
//        startActivity(intent)
    }

    override fun updateSearchResults(results: List<String>) {

    }
}
