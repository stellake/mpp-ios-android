package com.jetbrains.handson.mpp.mobile.api

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration


@Serializable
data class Station(
    val name: String,
    val crs: String? = null,
    val nlc: String? = null,
    val id: Long,
    val aliases: List<String>,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isGroupStation: Boolean
)

@Serializable
data class StationsResponse(
    val stations: List<Station>
)

@ImplicitReflectionSerializer
@OptIn(UnstableDefault::class)
suspend fun HttpClient.getStations(
): StationsResponse {

    val json = Json(JsonConfiguration(ignoreUnknownKeys = true, isLenient = true))

    // Get the content of an URL.
    val response: StationsResponse =
        get<StationsResponse>("https://mobile-api-dev.lner.co.uk/v1/stations")

    return response
}
