package com.jetbrains.handson.mpp.mobile

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext

@Serializable
data class OutboundJourney(val departureTime: String, val arrivalTime: String) {}

@Serializable
data class SerializableResponse(val numberOfAdults: Int, val numberOfChildren: Int, val outboundJourneys: List<OutboundJourney>) {}

class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private lateinit var view: ApplicationContract.View
    private val job: Job = SupervisorJob()

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
//        view.setLabel(createApplicationScreenMessage())
    }

    private fun serializableResponseToStringArray(serializableResponse: SerializableResponse): List<String> {
        var result: MutableList<String> = mutableListOf<String>()
        for (journey in serializableResponse.outboundJourneys) {
            result.add("${journey.departureTime} - ${journey.arrivalTime}")
        }
        return result
    }

    val client = HttpClient() {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json { this.ignoreUnknownKeys = true; this.isLenient = true})
        }
    }


    fun updateSearchResults(results: List<String>) {
        this.view?.setLabel("a")
        this.view?.updateSearchResults(results)
    }

    public override fun getTrainTimes(departureStation: String, arrivalStation: String) {
        this.view.updateSearchResults(listOf("Loading..."))
       launch {
            val response: SerializableResponse = client.request("https://mobile-api-softwire2.lner.co.uk/v1/fares?originStation=$departureStation&destinationStation=$arrivalStation&noChanges=false&numberOfAdults=2&numberOfChildren=0&journeyType=single&outboundDateTime=2021-07-24T14%3A30%3A00.000%2B01%3A00&outboundIsArriveBy=false")
            /*val response: SerializableResponse = client.request("https://mobile-api-softwire2.lner.co.uk/v1/fares/") {
                method = HttpMethod.Get
                parameter("originStation", departureStation)
                parameter("destinationStation", arrivalStation)
                parameter("noChanges", "false")
                parameter("numberOfAdults", "1")
                parameter("numberOfChildren", "0")
                parameter("journeyType", "single")
                parameter("outboundDateTime", "2021-07-24T14%3A30%3A00.000%2B01%3A00")
                parameter("outboundIsArriveBy", "false")
            }*/
           updateSearchResults(
                serializableResponseToStringArray(response))
        }
    }

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

}
