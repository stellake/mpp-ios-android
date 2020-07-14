package com.jetbrains.handson.mpp.mobile

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.http.Url
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var view: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()

    private var stationList = StationList(listOf())
    private val stationMap = mutableMapOf<String, String>()

    private fun getStations(view: ApplicationContract.View) {
        launch(coroutineContext) {
            val client = HttpClient {
                install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
            }
            stationList = client.get(Url("https://mobile-api-dev.lner.co.uk/v1/stations"))
            client.close()
            generateStationMap()
            view.updateDropDowns()
        }
    }

    private fun generateStationMap() {
        stationMap.clear()
        for (station in stationList.stations)
            stationMap[station.name] = station.crs ?: station.nlc
    }

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setLabel(createApplicationScreenMessage())
        getStations(view)
    }

    override fun getTimesRequest(departure: String, destination: String): String {
        return "https://mobile-api-dev.lner.co.uk/v1/fares?originStation=${departure}&destinationStation=${destination}&noChanges=false&numberOfAdults=2&numberOfChildren=0&journeyType=single&inboundDateTime=2020-07-01T12:16:27.371&inboundIsArriveBy=false&outboundDateTime=2020-07-14T19%3A30%3A00.000%2B01%3A00&outboundIsArriveBy=false"
    }

    override fun getStationNames(): List<String> {
        val retList = mutableListOf<String>()
        for (station in stationList.stations)
            retList.add(station.name)
        return retList
    }

    override fun getStationCode(name: String): String {
        return stationMap[name] ?: throw Exception("Station cannot be found in system.")
    }
}
