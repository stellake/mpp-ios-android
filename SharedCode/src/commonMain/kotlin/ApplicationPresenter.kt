package com.jetbrains.handson.mpp.mobile

import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var viewRef: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.viewRef = view
        view.setStations(stations)
    }

    override fun onSubmitPressed() {
        val view = this.viewRef!!
        val stationFrom = getShortStationName(view.getStationFrom())
        val stationTo = getShortStationName(view.getStationTo())
        if (stationFrom == stationTo) {
            view.createAlert("No ticket needed!")
            return
        }
        //view.openLink(getFullUrl(stationFrom, stationTo))
        val currentDateTime = DateTime.fromUnix(view.getCurrentUnixTime()).plus(TimeSpan(10 * 60 * 1000.0))
        val urlString = ApiUrlBuilder(stationFrom, stationTo)
            .withOutboundDate(currentDateTime)
            .build()
        println("API URL: $urlString")
        launch {
            val apiResult = getAPIResponse(urlString)
            view.displayTrainTimes(apiResult.toTrainTimes())
        }
    }
}
