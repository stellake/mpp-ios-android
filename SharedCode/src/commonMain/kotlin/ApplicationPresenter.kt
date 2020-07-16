package com.jetbrains.handson.mpp.mobile

import com.soywiz.klock.*
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
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
    private val client = HttpClient() {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json.nonstrict)
        }
    }
    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setLabel(createApplicationScreenMessage())
        launch { populateStationCRS() }
    }

    override fun onStationsSubmitted(departure: String, arrival: String) {
        val arriveCRS = stationToCRS(arrival)
        val departCRS = stationToCRS(departure)
        launch { callOnTrainPage(departCRS, arriveCRS) }
    }


    private suspend fun fetchStations(): StationList {
        val response = client.get<StationList>("https://mobile-api-dev.lner.co.uk/v1/stations")
        return response
    }

    private suspend fun populateStationCRS() {
        if (stationCRS.isEmpty()) {
            try {
                fetchStations().stations.forEach {
                    if (it.crs != null) {
                        stationCRS[it.name] = it.crs
                    }
                }
            }catch (e:ClientRequestException){
                view?.showAPIError("Bad API call")
            }
        }
        view?.updateStations(stationCRS.keys.toList())
    }

    private fun stationToCRS(station: String): String {
        val crs=stationCRS[station]
        if (crs==null){
            println("CRS lookup failed!")
            throw RuntimeException("LOOKUP FAILURE - STATION - $station")
        }
        return crs
    }


    fun readableTime(horrificTime: String): String {
        val dateTime = returnedFormat.parse(horrificTime.substringBeforeLast('+'))
        return dateTime.format(niceFormat)
    }

    suspend fun fetchJourneyData(
        originCRS: String,
        destinationCRS: String,
        outboundTime: DateTimeTz
    ): trainData {
        val outboundStr = outboundTime.format(dateFormat)
        val adults = "2"
        val children = "0"
        val trainInfo = client.get<trainData>(
            "https://mobile-api-dev.lner.co.uk/v1/fares?originStation=$originCRS&destinationStation=$destinationCRS&outboundDateTime=$outboundStr&numberOfChildren=$children&numberOfAdults=$adults&doSplitTicketing=false"
        )
        return trainInfo
    }

    suspend fun callOnTrainPage(originCRS: String, destinationCRS: String) {
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
            val trainInfo:trainData
            try {
                trainInfo = fetchJourneyData(originCRS, destinationCRS, now)
            }catch (e:ClientRequestException){
                view?.showAPIError("bad API call")
                return
            }
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







