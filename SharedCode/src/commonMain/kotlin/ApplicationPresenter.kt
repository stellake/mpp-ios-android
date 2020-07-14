package com.jetbrains.handson.mpp.mobile

import io.ktor.client.HttpClient
import io.ktor.client.request.get
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
            onStationsSubmitted(arriveDepart.second,arriveDepart.first)
        }
    }

    override fun onStationsSubmitted(departure: String, arrival: String) {
        val arriveCRS = stationToCRS(arrival)
        val departCRS = stationToCRS(departure)
        view?.openURL("https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/$departCRS/$arriveCRS/#LiveDepResults")
    }

    override fun stationToCRS(station: String): String {
        return DIRTY_CRS_HACK[station] ?: "KGX" //the world is king's cross
    }

    suspend fun callOnTrainPage(){
        val client = HttpClient()

        val trainInfo = client.get<String>("https://mobile-api-dev.lner.co.uk/v1/fares?originStation=CBG&destinationStation=HML&outboundDateTime=2020-08-05T12%3A16%3A27.371%2B00%3A00&numberOfChildren=1&numberOfAdults=2&doSplitTicketing=false"
        )



        client.close()

        view.showData
    }


}







