package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(), ApplicationContract.View,

    AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val presenter = ApplicationPresenter()
        presenter.onViewTaken(this)

        val spinner1: Spinner = findViewById(station_spinner1.id)
        ArrayAdapter.createFromResource(
            this,
            R.array.stations_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner1.adapter = adapter
        }

        val spinner2: Spinner = findViewById(station_spinner2.id)
        ArrayAdapter.createFromResource(
            this,
            R.array.stations_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner2.adapter = adapter
        }

        val button: Button = findViewById(station_button.id)
        button.setOnClickListener {
            val origin = spinner1.selectedItem.toString()
            val destination = spinner2.selectedItem.toString()
            /*val webIntent: Intent =
                Uri.parse(presenter.onButtonPressed(origin, destination)).let { webpage ->
                    Intent(Intent.ACTION_VIEW, webpage)
                }
            startActivity(webIntent)*/
            presenter.onButtonPressed(origin, destination)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, arg1: View?, position: Int, id: Long) {
        parent?.getItemAtPosition(position)

    }

    override fun setLabel(text: String) {
        findViewById<TextView>(R.id.main_text).text = text
    }

    override fun showData(text: FaresResponse) {
    }

    override fun showAlert(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }


}
