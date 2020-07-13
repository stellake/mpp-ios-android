package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var view: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setLabel(createApplicationScreenMessage())
    }

    override fun getTimesRequest(departure: String, destination: String): String {
        return "https://mobile-api-dev.lner.co.uk/v1/fares?originStation=${departure}&destinationStation=${destination}&noChanges=false&numberOfAdults=2&numberOfChildren=0&journeyType=single&inboundDateTime=2020-07-01T12:16:27.371&inboundIsArriveBy=false&outboundDateTime=2020-07-14T19%3A30%3A00.000%2B01%3A00&outboundIsArriveBy=false"
    }
}
