package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun setLabel(text: String)
        fun updateDropDowns()
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun getTimesRequest(departure: String, destination: String): String
        abstract fun getStationNames(): List<String>
        abstract fun getStationCode(name: String): String
    }
}
