package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun setLabel(text: String)
        fun openURL(url:String)
        fun getArrivalDepartureStations():Pair<String,String>
    }



    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun onDoneButtonPressed()
        abstract fun stationToCRS(station: String): String
    }
}
