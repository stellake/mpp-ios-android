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

    override fun runSearch() {
        TODO()
    }
}
