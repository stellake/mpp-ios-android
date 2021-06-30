package com.jetbrains.handson.mpp.mobile

import com.jetbrains.handson.mpp.mobile.models.ApiReply
import com.jetbrains.handson.mpp.mobile.models.OutboundJourneys
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext


class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var view: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()

    private lateinit var outboundJourneys: List<OutboundJourneys>

    private val client = HttpClient() {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            })
        }
    }

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setLabel(createApplicationScreenMessage())
    }

    fun requestFromAPI(departureCode: String, arrivalCode: String): List<OutboundJourneys> {
        val request = launch {
            val valResponse : ApiReply = client.get(
                    "https://mobile-api-softwire2.lner.co.uk/v1/fares?originStation=LDS&destinationStation=KGX&noChanges=false&numberOfAdults=2&numberOfChildren=0&journeyType=single&outboundDateTime=2021-07-24T14%3A30%3A00.000%2B01%3A00&outboundIsArriveBy=false"
            )
            outboundJourneys = valResponse.outboundJourneys
        }
    }
}
