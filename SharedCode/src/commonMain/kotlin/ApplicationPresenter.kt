package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.*
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
        return if (originCode != null && destinationCode != null) {
            "https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/$originCode/$destinationCode/#LiveDepResults"
        } else {
            "https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/EUS/BHM/#LiveDepResults"
        }
    }



}
