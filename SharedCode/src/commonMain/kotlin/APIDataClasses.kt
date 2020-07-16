package com.jetbrains.handson.mpp.mobile

import kotlinx.serialization.Serializable

@Serializable
data class trainData(val outboundJourneys: List<JourneyOption>)

@Serializable
data class JourneyOption(
    val departureTime: String,
    val arrivalTime: String,
    val tickets: List<ticketOptions>
)

@Serializable
data class ticketOptions(
    val priceInPennies: Int
)

@Serializable
data class StationList(
    val stations: List<Station>
)

@Serializable
data class Station(
    val name: String,
    val crs: String?
)