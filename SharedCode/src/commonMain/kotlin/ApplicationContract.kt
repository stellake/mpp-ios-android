package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun setLabel(text: String)
        fun setFromSpinnerContent(list: List<String>)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
    }
}
