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
        fun setTitle(title: String, subtitle: String)

        /**
         * Sets the contents of the spinners/pickers to a list of station codes
         */
        fun setStations(stations: List<String>)

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
