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
            view.updateDropDowns(getStationNames().sorted())
        }
    }

    private fun getStationNames(): List<String> {
        return stationList.stations.map { it.name }
    }

    private fun generateStationMap() {
        stationMap.clear()
        for (station in stationList.stations)
            stationMap[station.name] = station.crs ?: station.nlc
    }

    private fun getStationCode(name: String): String {
        return stationMap[name] ?: throw Exception("Station cannot be found in system.")
    }

//    private fun DisplayJourney(journeyOption: JourneyOption):String{
//        var label = ""
//        for(ticket in journeyOption.tickets) {
//            label = label + ("The price is £" + ticket.priceInPennies.toDouble() / 100 + "\n")
//        }
//        return label
//    }

    private fun convertToLight(journey: JourneyOption): String {
        val time = journey.arrivalTime
        val pennies = journey.tickets[0].priceInPennies
        val price = "£${pennies/100}.${pennies - (pennies/100)*100}"
        return "$time - $price"
    }

    private fun getJourneyDetailsLight(fares: Fares): List<JourneyDetailsLight> {
        return fares.outboundJourneys.map { JourneyDetailsLight(it.journeyOptionToken, convertToLight(it)) }
    }

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setLabel(createApplicationScreenMessage())
        getStations(view)
    }

    @kotlinx.serialization.UnstableDefault
    override fun loadJourneys(view: ApplicationContract.View, departure: String, destination: String) {
        launch(coroutineContext) {

            view.setButtonAvailability(false)

            val client = HttpClient {
                install(JsonFeature) {
                    serializer = KotlinxSerializer(kotlinx.serialization.json.Json{
                        ignoreUnknownKeys = true
                    })
                }
            }

            var fares: Fares

            try {
                fares = client.get(Url("https://mobile-api-dev.lner.co.uk/v1/fares?originStation=${getStationCode(departure)}&destinationStation=${getStationCode(destination)}&outboundDateTime=2020-07-15T12%3A16%3A27.371%2B00%3A00&numberOfChildren=2&numberOfAdults=2&doSplitTicketing=false"))
                if (fares.outboundJourneys.isEmpty()) throw Exception("No journeys available.")
                view.displayFares(getJourneyDetailsLight(fares))
            } catch (e: Exception) {
                view.setLabel("Sorry we couldn't find a journey.")
                println(e.message)
            }

            client.close()

//            view.setButtonAvailability(true)
//            var label = ""
//            for(journey in fares.outboundJourneys){
//                label = label + DisplayJourney(journey)
//            }
//
//            print(label)
        }
    }
}
