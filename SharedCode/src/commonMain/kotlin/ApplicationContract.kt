package com.jetbrains.handson.mpp.mobile

import com.jetbrains.handson.mpp.mobile.api.FaresResponse
import com.jetbrains.handson.mpp.mobile.api.JourneyOption
import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun setLabel(text: String)
        fun showAlert(text: String)
        fun showData(text: List<JourneyOption>)
        fun openWebpage(url: String)
    }

    abstract class Presenter : CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun onButtonPressed(origin: String, destination: String, time: String)
    }
}
