package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * The main view for the Android app
 */
class MainActivity : AppCompatActivity(), ApplicationContract.View {

    private lateinit var presenter: ApplicationContract.Presenter

    private lateinit var fromSpinner: Spinner
    private lateinit var toSpinner: Spinner
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get references to views
        fromSpinner = findViewById(R.id.from_spinner)
        toSpinner = findViewById(R.id.to_spinner)
        searchButton = findViewById(R.id.search_btn)

        // add event listeners
        searchButton.setOnClickListener {
            presenter.runSearch(
                    fromSpinner.selectedItem as String,
                    toSpinner.selectedItem as String
            )
        }

        // get reference to presenter and tell it we're done
        presenter = ApplicationPresenter()
        presenter.onViewTaken(this)
    }

    override fun setTitle(title: String, subtitle: String) {
        findViewById<TextView>(R.id.main_text).text = title
        findViewById<TextView>(R.id.sub_header).text = subtitle
    }

    override fun setStations(stations: List<String>) {
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, stations)
        fromSpinner.adapter = adapter
        toSpinner.adapter = adapter
        toSpinner.setSelection(1)
    }

    override fun openUrl(url: String) {
        val page = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, page)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun displayErrorMessage(message: String) {
       Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun displayJourneys(journeyCollection: JourneyCollection) {
        val adapter = RecyclerViewAdapter(journeyCollection)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

}


class RecyclerViewAdapter(private val dataSet: JourneyCollection) : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.resultText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.result_item, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerViewHolder, position: Int) {
        viewHolder.textView.text = dataSet.outboundJourneys[position].departureTime
    }

    override fun getItemCount() = dataSet.outboundJourneys.size
}