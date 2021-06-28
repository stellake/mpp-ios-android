package com.jetbrains.handson.mpp.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ApplicationContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val presenter = ApplicationPresenter()
        presenter.onViewTaken(this)


    }

    override fun setLabel(text: String) {
//        findViewById<TextView>(R.id.main_text).text = text
    }

    fun onSubmitButtonTapped(view: View) {
        
    }


}
