package com.jetbrains.handson.mpp.mobile

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

data class Station(val fullName: String, val shortName: String)

val stations = arrayOf(
    Station("King's Cross", "KGX"),
    Station("Edinburgh", "EDB"),
    Station("Manchester", "MAN")
)

expect fun platformName(): String

fun getShortStationName(index: Int): String {
    return stations[index].shortName
}

fun getFullUrl(station_from: String, station_to: String): String {
    return "https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/${station_from}/${station_to}"
}

suspend fun getAPIResponseString(apiUrl: String): String {
    val client = HttpClient { install(JsonFeature) {
        serializer = KotlinxSerializer()
    }}
    return client.get(apiUrl) as String
}

@OptIn(UnstableDefault::class)
fun deserialiseJson(jsonString: String): ApiResult {
    val jsonConfig = JsonConfiguration(ignoreUnknownKeys = true)
    val json = Json(jsonConfig)
    return json.parse(ApiResult.serializer(), jsonString)
}

fun getEpochFromUTC(s: String): Long {
    // NOTE: Not completely accurate
    // TODO: make better?
    val year: Long = s.substring(0,4).toLong()
    val month: Long = s.substring(5,7).toLong()
    val day: Long = s.substring(8,10).toLong()
    val hour: Long = s.substring(11,13).toLong()
    return 365*24*60*60*(year-1970) + 31*24*60*60*(month-1) + 24*60*60*day + 60*60*hour
}
fun getPrice(tickets: List<ApiResult.Journey.Ticket>): Int {
    return tickets.map { it.priceInPennies }.sum()
}

fun ApiResult.toTrainTimes(): TrainTimes{
    val journeys = mutableListOf<TrainTimes.Journey>()
    this.outboundJourneys.forEach { journey ->
        journeys.plusAssign(TrainTimes.Journey(
            getPrice(journey.tickets),
            getEpochFromUTC(journey.departureTime),
            getEpochFromUTC(journey.arrivalTime),
            journey.legs.size-1
        ))
    }
    return TrainTimes(
        this.outboundJourneys.firstOrNull()?.originStation?.displayName ?: "NONE",
        this.outboundJourneys.lastOrNull()?.originStation?.displayName ?: "NONE",
        journeys.toTypedArray())
}