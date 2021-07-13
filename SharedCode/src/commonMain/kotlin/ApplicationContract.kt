package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

/**
 * This contract specifies what methods a view must implement, and what methods the presenter
 * exposes to the view.
 */
interface ApplicationContract {
    interface View {
        /**
         * Sets the heading and subheading for the window.
         */
        fun setLabel(main_text: String, sub_header: String)

        /**
         * Sets the content of the spinner containing the possible 'from' destinations.
         */
        fun setFromSpinnerContent(list: List<String>)

        /**
         * Sets the content of the spinner containing the possible 'to' destinations.
         */
        fun setToSpinnerContent(list: List<String>)

        /**
         * Opens a URL in the browser.
         */
        fun openUrl(url: String)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun runSearch(from: String, to: String)
    }
}
