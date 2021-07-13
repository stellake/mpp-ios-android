package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * The main presenter for the app. Acts as a controller for views and calls the main application
 * logic, most of which is contained within ./common.kt
 */
class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private val job: Job = SupervisorJob()
    private lateinit var view: ApplicationContract.View

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    /**
     * Called when a new view is loaded.
     */
    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setLabel(createApplicationScreenMessage(), createSubHeaderMessage())
        view.setFromSpinnerContent(createStationList())
        view.setToSpinnerContent(createStationList())
    }

    /**
     * Runs an API call which returns information about the fares between two stations.
     *
     * TODO: Call API using Ktor instead of opening link.
     */
    override fun runSearch(from: String, to: String) {
        view.openUrl("https://mobile-api-softwire2.lner.co.uk/v1/fares?originStation=$from&destinationStation=$to&noChanges=false&numberOfAdults=2&numberOfChildren=0&journeyType=single&outboundDateTime=2021-07-24T14%3A30%3A00.000%2B01%3A00&outboundIsArriveBy=false")
    }

}
