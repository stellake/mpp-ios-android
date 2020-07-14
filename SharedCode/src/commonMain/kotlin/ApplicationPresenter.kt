package com.jetbrains.handson.mpp.mobile

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import io.ktor.client.statement.readText
import kotlinx.coroutines.*
import kotlinx.serialization.serializer
import kotlin.coroutines.CoroutineContext

class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var view: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()
    private val codeMap = mapOf<String, String>("Harrow and Wealdstone" to  "HRW",
        "Canley" to "CNL",
        "London Euston" to "EUS",
        "Coventry" to "COV",
        "Birmingham New Street" to "BHM")
    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setLabel(createApplicationScreenMessage())
    }

    override fun onButtonPressed(origin: String, destination: String): String {
        val originCode = codeMap[origin]
        val destinationCode = codeMap[destination]
        var output = "a"
        GlobalScope.launch(dispatchers.io) {

            output = sequentialRequests() }
        return output
        /*return if (originCode != null && destinationCode != null) {
            "https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/$originCode/$destinationCode/#LiveDepResults"
        } else {
            "https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/EUS/BHM/#LiveDepResults"
        }*/
    }

    suspend fun sequentialRequests(): String {
        val client = HttpClient() {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
        // Get the content of an URL.
        val output: HttpResponse = client.get<HttpResponse>("https://mobile-api-dev.lner.co.uk/v1/fares?originStation=EUS&destinationStation=BHM&outboundDateTime=2020-07-15T12%3A16%3A27.371%2B00%3A00&inboundDateTime=2020-03-06T12%3A16%3A27.371%2B00%3A00&numberOfChildren=1&numberOfAdults=0&doSplitTicketing=false")
        println("Hi")
        println(output.readText())

        return output.readText()
    }


}
