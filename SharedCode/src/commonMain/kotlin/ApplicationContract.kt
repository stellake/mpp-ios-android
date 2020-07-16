package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    data class TrainJourney(val departureTime:String,val arrivalTime:String,val cost:Int)
    interface View {
        fun setLabel(text: String)
        fun openURL(url:String)
        fun showData(data:List<TrainJourney>)
        fun updateStations(data: List<String>)
        fun showAPIError(info:String) //print info in console, show something nice to the user
    }



    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun onStationsSubmitted(departure: String,arrival:String)
    }
}
