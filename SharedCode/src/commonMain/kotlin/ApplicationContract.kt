package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun updateDropDowns(stationNames: List<String>)
        fun setButtonAvailability(state: Boolean)
        fun displayFares(fareList: List<JourneyDetailsLight>)
        fun showAlert(message: String)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun loadJourneys(view: View, departure: String, destination: String)
    }
}
