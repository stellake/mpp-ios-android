package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    data class TrainJourney(val departureTime:String,val arrivalTime:String,val cost:Int)
    interface View {
        fun setLabel(text: String)
        fun openURL(url:String)
        fun getArrivalDepartureStations():Pair<String,String>
        fun showData(data:List<TrainJourney>)
    }



    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun onDoneButtonPressed()
        abstract fun onStationsSubmitted(departure: String,arrival:String)
        abstract fun getStationList():List<String>
    }
}
