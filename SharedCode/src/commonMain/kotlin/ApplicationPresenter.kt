package com.jetbrains.handson.mpp.mobile

import com.soywiz.klock.*
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
    private val stationCRS = mutableMapOf<String, String>()
    private val dateFormat = DateFormat("yyyy-MM-ddTHH%3Amm%3Ass.000%2B01%3A00")
    private val returnedFormat = DateFormat("yyyy-MM-ddTHH:mm:ss.000")
    private val niceFormat = DateFormat("HH:mm")
    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setLabel(createApplicationScreenMessage())
        launch { populateStationCRS() }
    }

    override fun onDoneButtonPressed() {
        val arriveDepart = view?.getArrivalDepartureStations()
        if (arriveDepart != null) {
            onStationsSubmitted(arriveDepart.second, arriveDepart.first)
        }
    }

    override fun onStationsSubmitted(departure: String, arrival: String) {
        val arriveCRS = stationToCRS(arrival)
        val departCRS = stationToCRS(departure)
        launch { callOnTrainPage(departCRS, arriveCRS) }
    }

    @Serializable
    data class StationList(
        val stations: List<Station>
    )

    @Serializable
    data class Station(
        val name: String,
        val crs: String?
    )

    private suspend fun populateStationCRS() {
        if (stationCRS.isEmpty()) {
            val client = HttpClient() {
                install(JsonFeature) {
                    serializer = KotlinxSerializer(Json.nonstrict)
                }
            }
            val job = client.get<StationList>("https://mobile-api-dev.lner.co.uk/v1/stations")
            job.stations.forEach {
                if (it.crs != null) {
                    stationCRS[it.name] = it.crs
                }
            }
        }
        view?.updateStations(stationCRS.keys.toList())
    }

    private fun stationToCRS(station: String): String {
        return stationCRS[station] ?: "KGX" //the world is king's cross
    }

    @Serializable
    data class trainData(val outboundJourneys: List<JourneyOption>)

    @Serializable
    data class JourneyOption(
        val departureTime: String,
        val arrivalTime: String,
        val tickets: List<ticketOptions>
    )

    @Serializable
    data class ticketOptions(
        val priceInPennies: Int
    )


    fun readableTime(horrificTime: String): String {
        val dateTime = returnedFormat.parse(horrificTime.substringBeforeLast('+'))
        return dateTime.format(niceFormat)
    }

    suspend fun callOnTrainPage(originCRS: String, destinationCRS: String) {
        val client = HttpClient() {
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json.nonstrict)
            }
        }
        val now = DateTimeTz.nowLocal().addOffset(TimeSpan(10000.0))
        val journeys: List<ApplicationContract.TrainJourney>
        if (originCRS == destinationCRS) {
            //you are already there
            journeys = listOf(
                ApplicationContract.TrainJourney(
                    now.format(niceFormat),
                    now.format(niceFormat),
                    0
                )
            )
        } else {
            val outboundTime = now.format(dateFormat)
            val adults = "2"
            val children = "1"

            val trainInfo = client.get<trainData>(
                "https://mobile-api-dev.lner.co.uk/v1/fares?originStation=$originCRS&destinationStation=$destinationCRS&outboundDateTime=$outboundTime&numberOfChildren=$children&numberOfAdults=$adults&doSplitTicketing=false"
            )


            client.close()
            journeys = mutableListOf<ApplicationContract.TrainJourney>()
            trainInfo.outboundJourneys.forEach {
                val minPrice = it.tickets.minBy { it.priceInPennies }?.priceInPennies ?: 0
                journeys.add(
                    ApplicationContract.TrainJourney(
                        readableTime(it.departureTime),
                        readableTime(it.arrivalTime),
                        minPrice
                    )
                )
            }
        }
        view?.showData(journeys)
    }


}







