package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView

/**
 * The main view for the Android app
 */
class MainActivity : AppCompatActivity(), ApplicationContract.View {

    private lateinit var presenter: ApplicationContract.Presenter

    private lateinit var fromSpinner: Spinner
    private lateinit var toSpinner: Spinner
    private lateinit var searchButton: Button

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
    }

    override fun openUrl(url: String) {
        val page = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, page)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

}
