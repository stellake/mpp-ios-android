package com.jetbrains.handson.mpp.mobile.models

import kotlinx.serialization.Serializable

@Serializable
data class OutboundJourneys (
    val originStation : OriginStation,
    val destinationStation: DestinationStation,
    val tickets: Tickets
)