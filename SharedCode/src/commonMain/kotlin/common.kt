package com.jetbrains.handson.mpp.mobile

import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.readBytes
import io.ktor.client.statement.readText
import io.ktor.utils.io.core.use
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse

expect fun platformName(): String

fun createAppTitle(): String {
    return "Journey Planner"
}

fun createAppSubtitle(): String {
    return ""
}

/**
 * Creates the list of stations that the app has available.
 *
 * Currently these are represented as simple strings, since no other properties are associated
 * with them.
 */
fun createStations(): List<String> {
    return listOf("MAN", "LST", "STP", "CBG", "KGX")
}

@ImplicitReflectionSerializer
@UnstableDefault
suspend fun queryApi(from: String, to: String) : ApiResponse {
    try {
        val response: JourneyCollection = HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json {
                    ignoreUnknownKeys = true
                })
            }
        }.use { client ->
            client.get("https://mobile-api-softwire2.lner.co.uk/v1/fares") {
                parameter("originStation", from)
                parameter("destinationStation", to)
                parameter("noChanges", "false")
                parameter("numberOfAdults", 2)
                parameter("numberOfChildren", 0)
                parameter("journeyType", "single")
                parameter("outboundDateTime", "2021-07-24T15:15:00.000+01:00")
                parameter("outboundIsArriveBy", "false")
            }
        }
        return ApiResponse(response, null)
    } catch (e: ClientRequestException) {
        val apiError = Json.parse<ApiError>(e.response.readText())
        return ApiResponse(null, apiError)
    }
}

