package com.jetbrains.handson.mpp.mobile

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.cio.websocket.FrameType.Companion.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

expect fun platformName(): String

fun createApplicationScreenMessage(): String {

    return "Kotlin Rocks on ${platformName()}"
}

fun getApiCall():String {
    var content = ""

    GlobalScope.launch(){
        val client = HttpClient()
        content = client.get<String>("https://mobile-api-dev.lner.co.uk/v1/fares?originStation=1444&destinationStation=HML&viaStation=WFJ&avoidStation=HRW&outboundDateTime=2020-03-05T12%3A16%3A27.371%2B00%3A00&inboundDateTime=2020-03-06T12%3A16%3A27.371%2B00%3A00&numberOfChildren=2&numberOfAdults=2&doSplitTicketing=false&initialOutboundDateTime=2020-03-06T12%3A16%3A27.371%2B00%3A00&initialInboundDateTime=2020-03-06T12%3A16%3A27.371%2B00%3A00&promotionCode=SWOPEN20")
    }
    return content
}