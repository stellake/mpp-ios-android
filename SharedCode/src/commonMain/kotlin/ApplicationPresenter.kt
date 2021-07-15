package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlin.coroutines.CoroutineContext

/**
 * The main presenter for the app. Acts as a controller for views and calls the main application
 * logic, most of which is contained within ./common.kt
 */
@UnstableDefault
class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private val job: Job = SupervisorJob()
    private lateinit var view: ApplicationContract.View

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job
    private val coroutineScope = CoroutineScope(coroutineContext)

    /**
     * Views should call this when loaded.
     */
    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setTitle(createAppTitle())
        view.setStations(createStations())
    }

    /**
     * Runs an API call which returns information about the fares between two stations.
     */
    @ImplicitReflectionSerializer
    override fun runSearch(from: String, to: String) {
        coroutineScope.launch {
            withContext(dispatchers.io) {
                val apiResponse = queryApi(from, to)
                withContext(dispatchers.main) {
                    if (apiResponse.apiError == null) {
                        if (apiResponse.journeyCollection != null && apiResponse.journeyCollection.outboundJourneys.count() > 0) {
                            view.displayJourneys(apiResponse.journeyCollection)
                        } else {
                            view.displayErrorMessage("No suitable trains found.")
                        }
                    } else {
                        view.displayErrorMessage(apiResponse.apiError.error_description)
                    }
                }
            }
        }
    }

}

@Serializable
data class JourneyCollection(val outboundJourneys: List<Journey>)

@Serializable
data class Journey(
        val journeyId: String,
        val departureTime: String,
        val arrivalTime: String,
        val originStation: Station,
        val destinationStation: Station,
        val isFastestJourney: Boolean,
        val journeyDurationInMinutes: Int,
        val primaryTrainOperator: Map<String, String>,
        val status: String
)

@Serializable
data class Station(val displayName: String, val crs: String, val nlc: String)

@Serializable
data class ApiError(val error: String, val error_description: String)

@Serializable
data class ApiResponse(val journeyCollection: JourneyCollection?, val apiError: ApiError?)

