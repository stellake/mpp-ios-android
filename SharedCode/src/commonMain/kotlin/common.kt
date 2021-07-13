package com.jetbrains.handson.mpp.mobile

expect fun platformName(): String

fun createApplicationScreenMessage(): String {
    return "Kotlin Rocks on ${platformName()}"
}

fun createSubHeaderMessage(): String {
    return "Live Train Times"
}

/**
 * Creates the list of stations that the app has available.
 *
 * Currently these are represented as simple strings, since no other properties are associated
 * with them.
 */
fun createStationList(): List<String> {
    return listOf("ELS", "KTN", "STP", "CBG", "KGX")
}

