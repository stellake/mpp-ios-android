package com.jetbrains.handson.mpp.mobile.models

import kotlinx.serialization.Serializable

@Serializable
data class DestinationStation (
    val displayName: String,
    val crs: String
)