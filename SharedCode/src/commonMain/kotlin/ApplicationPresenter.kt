package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private lateinit var view: ApplicationContract.View
    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setLabel(createApplicationScreenMessage(), createSubHeaderMessage())
        view.setFromSpinnerContent(createStationList())
        view.setToSpinnerContent(createStationList())
    }

    override fun runSearch(from: String, to: String) {
        view.openUrl("https://mobile-api-softwire2.lner.co.uk/v1/fares?originStation=$from&destinationStation=$to&noChanges=false&numberOfAdults=2&numberOfChildren=0&journeyType=single&outboundDateTime=2021-07-24T14%3A30%3A00.000%2B01%3A00&outboundIsArriveBy=false")
    }
}
