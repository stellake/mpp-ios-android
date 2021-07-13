package com.jetbrains.handson.mpp.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView

class MainActivity : AppCompatActivity(), ApplicationContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val presenter = ApplicationPresenter()
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
}
