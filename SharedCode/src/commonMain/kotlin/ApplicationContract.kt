package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun setLabel(main_text: String, sub_header: String)
        fun setFromSpinnerContent(list: List<String>)
        fun setToSpinnerContent(list: List<String>)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
    }
}
