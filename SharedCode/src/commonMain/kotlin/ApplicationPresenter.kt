package com.jetbrains.handson.mpp.mobile

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlin.coroutines.CoroutineContext

class ApplicationPresenter : ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var view: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()
    private val codeMap = mapOf<String, String>(
        "Harrow and Wealdstone" to "HRW",
        "Canley" to "CNL",
        "London Euston" to "EUS",
        "Coventry" to "COV",
        "Birmingham New Street" to "BHM"
    )
    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setLabel("Get live train times")
    }

    @ImplicitReflectionSerializer
    @OptIn(UnstableDefault::class)

    override fun onButtonPressed(origin: String, destination: String, time: String) {
        val originCode = codeMap[origin]
        val destinationCode = codeMap[destination]
        if (originCode == null || destinationCode == null) {
            view?.showAlert("Error: Station not found")
            return
        }
        if (originCode == destinationCode) {
            view?.showAlert("Stations must be different")
            return
        }
        launch {
            val response = sequentialRequests(originCode, destinationCode, time)
            if (response != null) view?.showData(response)
        }
    }

    @ImplicitReflectionSerializer
    @OptIn(UnstableDefault::class)
    private suspend fun sequentialRequests(
        originCode: String,
        destinationCode: String, time: String
    ): FaresResponse? {
        val client = HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }

        val json = Json(JsonConfiguration(ignoreUnknownKeys = true, isLenient = true))
        var jsonResponse: FaresResponse? = null
        // Get the content of an URL.
        try {
            val response: HttpResponse =
                client.get<HttpResponse>("https://mobile-api-dev.lner.co.uk/v1/fares?originStation=$originCode&destinationStation=$destinationCode&outboundDateTime=$time.371%2B00%3A00&inboundDateTime=$time.371%2B00%3A00&numberOfChildren=1&numberOfAdults=0&doSplitTicketing=false")
            val parsedResponse = json.parseJson(response.readText())
            jsonResponse = json.fromJson<FaresResponse>(parsedResponse)
        } catch (cause: Throwable) {
            view?.showAlert("An error occurred:$cause")

        }
        client.close()
        return jsonResponse
    }

    fun onBuyButton(
        outbound: String,
        inbound: String,
        month: Int,
        day: Int,
        hour: Int,
        minutes: Int,
        returnBool: Boolean = true
    ) {
        val returnSymbol = if (returnBool) {
            "y"
        } else {
            "n"
        }
        view?.openWebpage("https://www.lner.co.uk/buy-tickets/booking-engine/?ocrs=$outbound&dcrs=$inbound&outm=$month&outd=$day&outh=$hour&outmi=$minutes&ret=$returnSymbol")
    }
}
