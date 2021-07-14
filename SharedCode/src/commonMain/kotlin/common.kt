package com.jetbrains.handson.mpp.mobile

expect fun platformName(): String

fun createAppTitle(): String {
    return "Journey Planner"
}

fun createAppSubtitle(): String {
    return ""
}

/**
 * Creates the list of stations that the app has available.
 *
 * Currently these are represented as simple strings, since no other properties are associated
 * with them.
 */
fun createStations(): List<String> {
    return listOf("ELS", "KTN", "STP", "CBG", "KGX")
}

