package com.jetbrains.handson.mpp.mobile

expect fun platformName(): String

fun getStations(): Array<String> {
    return arrayOf("KGX", "EDB", "Man")
}
