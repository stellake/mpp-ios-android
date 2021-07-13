package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView

/**
 * The main view for the Android app
 */
class MainActivity : AppCompatActivity(), ApplicationContract.View {

    private lateinit var presenter: ApplicationContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = ApplicationPresenter()
        presenter.onViewTaken(this)
    }

    override fun setLabel(main_text: String, sub_header: String) {
        findViewById<TextView>(R.id.main_text).text = main_text
        findViewById<TextView>(R.id.sub_header).text = sub_header
    }

    override fun setFromSpinnerContent(list: List<String>) {
        val spinner: Spinner = findViewById(R.id.from_spinner)
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list)
        spinner.adapter = adapter
    }

    override fun setToSpinnerContent(list: List<String>) {
        val spinner: Spinner = findViewById(R.id.to_spinner)
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list)
        spinner.adapter = adapter
    }

    override fun openUrl(url: String) {
        val page = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, page)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    /**
     * Event handler for search button.
     */
    fun onSearchBtnClick(view: View) {
        val fromSpinner: Spinner = findViewById(R.id.from_spinner)
        val toSpinner: Spinner = findViewById(R.id.to_spinner)
        presenter.runSearch(fromSpinner.selectedItem as String, toSpinner.selectedItem as String)
    }

}
