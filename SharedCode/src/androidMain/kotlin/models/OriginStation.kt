package com.jetbrains.handson.mpp.mobile.models

import kotlinx.serialization.Serializable

@Serializable
data class OriginStation (
        val displayName : String,
        val crs : String
)