package com.jetbrains.handson.mpp.mobile

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import kotlinx.coroutines.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonElement
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
        view.setLabel("Get live train times")
    }

    @ImplicitReflectionSerializer
    @OptIn(UnstableDefault::class)

    override fun onButtonPressed(origin: String, destination: String) {
        val originCode = requireNotNull(codeMap[origin])
        val destinationCode = requireNotNull(codeMap[destination])

        launch {
            val response = sequentialRequests(originCode, destinationCode)
            println(response)
            view?.showData(response)
        }
    }

    @ImplicitReflectionSerializer
    @OptIn(UnstableDefault::class)
    private suspend fun sequentialRequests(originCode: String, destinationCode: String): FaresResponse {
        val client = HttpClient() {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }

        val json = Json(JsonConfiguration(ignoreUnknownKeys = true, isLenient = true))

        // Get the content of an URL.
        val output: Deferred<HttpResponse> = async {
            client.get<HttpResponse>("https://mobile-api-dev.lner.co.uk/v1/fares?originStation=$originCode&destinationStation=$destinationCode&outboundDateTime=2020-07-15T12%3A16%3A27.371%2B00%3A00&inboundDateTime=2020-03-06T12%3A16%3A27.371%2B00%3A00&numberOfChildren=1&numberOfAdults=0&doSplitTicketing=false")
        }
        println("Hi")
        val output2 = output.await().readText()
        client.close()
        val output3 = json.parseJson(output2)
        return json.fromJson<FaresResponse>(output3)
    }
}
