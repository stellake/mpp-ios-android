package com.jetbrains.handson.mpp.mobile

expect fun platformName(): String

fun createApplicationScreenMessage(): String {
    return "Kotlin Rocks on ${platformName()}"
}

fun createSubHeaderMessage(): String {
    return "Live Train Times"
}

fun createStationList(): List<String> {
    return listOf("ELS", "KTN", "STP", "CBG", "KGX")
}

