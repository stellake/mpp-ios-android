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
        view.setStations(getStations())
    }

    override fun onSubmitPressed(view: ApplicationContract.View) {
        val stations = getStations()
        val stationFrom = stations[view.getStationFrom()]
        val stationTo = stations[view.getStationTo()]
        view.openLink(getFullUrl(stationFrom, stationTo))
    }
}
