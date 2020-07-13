package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ApplicationPresenter : ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var view: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()

    private val DIRTY_CRS_HACK = mapOf(
        "Cambridge" to "CBG",
        "King's Cross" to "KGX",
        "Durham" to "DHM",
        "Edinburgh Waverly" to "EDB",
        "York" to "YRK"
    )

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setLabel(createApplicationScreenMessage())
    }

    override fun onDoneButtonPressed() {
        val arriveDepart = view?.getArrivalDepartureStations()
        if (arriveDepart!=null) {
            val arriveCRS = stationToCRS(arriveDepart.first)
            val departCRS = stationToCRS(arriveDepart.second)
            view?.openURL("https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/$departCRS/$arriveCRS/#LiveDepResults")
        }
    }

    override fun stationToCRS(station: String): String {
        return DIRTY_CRS_HACK[station] ?: "KGX" //the world is king's cross
    }
}
