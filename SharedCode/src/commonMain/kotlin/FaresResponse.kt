package com.jetbrains.handson.mpp.mobile

import kotlinx.serialization.Serializable

@Serializable
data class FaresResponse(
    val outboundJourneys: List<JourneyOption>
)

@Serializable
data class JourneyOption(
    val journeyOptionToken: String,
    val journeyId: String,
    val originStation: Station,
    val destinationStation: Station,
    val departureTime: String,
    val arrivalTime: String,
    val journeyDurationInMinutes: Int
)

@Serializable
data class Station(
    val displayName: String,
    val crs: String,
    val nlc: String
)