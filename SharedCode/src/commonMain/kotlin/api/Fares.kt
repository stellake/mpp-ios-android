package com.jetbrains.handson.mpp.mobile.api

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault

@Serializable
data class FaresResponse(
    val outboundJourneys: List<JourneyOption>
)

@Serializable
data class JourneyOption(
    val journeyOptionToken: String,
    val journeyId: String,
    val originStation: JourneyOptionStation,
    val destinationStation: JourneyOptionStation,
    val departureTime: String,
    val arrivalTime: String,
    val journeyDurationInMinutes: Int
)

@Serializable
data class JourneyOptionStation(
    val displayName: String,
    val crs: String,
    val nlc: String

)

@ImplicitReflectionSerializer
@OptIn(UnstableDefault::class)
suspend fun HttpClient.getFares(
    originCode: String,
    destinationCode: String,
    time: String
): FaresResponse? {
    // Get the content of an URL.
    val response: FaresResponse? =
        get<FaresResponse?>("https://mobile-api-dev.lner.co.uk/v1/fares?originStation=$originCode&destinationStation=$destinationCode&outboundDateTime=$time%2B00%3A00&inboundDateTime=$time%2B00%3A00&numberOfChildren=1&numberOfAdults=0&doSplitTicketing=false")
    return response
}