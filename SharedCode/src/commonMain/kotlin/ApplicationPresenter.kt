package com.jetbrains.handson.mpp.mobile

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
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

    @Serializable
    data class trainData(val outboundJourneys: List<JourneyOption>,
                         val inboundJourneys: List<JourneyOption>
                         )

    @Serializable
    data class JourneyOption(val departureTime: String,
                             val arrivalTime: String,
                             val tickets: List<ticketOptions>
    )

    @Serializable
    data class ticketOptions(val priceInPennies: Int
    )






    suspend fun callOnTrainPage(){
        val client = HttpClient() {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }

        val originCRS = "CBG"
        val destinationCRS = "KGX"
        val outboundTime = "2020-08-05" + "T" + "12" + "%3A" + "16" + "%3A" + "27.371" + "%2B" + "00" + "%3A" + "00"
        val adults = "2"
        val children = "1"

        val trainInfo = client.get<trainData>("https://mobile-api-dev.lner.co.uk/v1/fares?originStation=$originCRS&destinationStation=$destinationCRS&outboundDateTime=$outboundTime&numberOfChildren=$children&numberOfAdults=$adults&doSplitTicketing=false"
        )


        client.close()

        view?.showData(listOf())
    }


}







