package com.jetbrains.handson.mpp.mobile.api

import com.soywiz.klock.DateFormat
import com.soywiz.klock.parse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.serialization.*

@Serializable
data class FaresResponse(
    val outboundJourneys: List<JourneyOption>
)

@Serializable
data class DateTime(val dateTime: com.soywiz.klock.DateTimeTz){
    @Serializer(forClass = DateTime::class)
    companion object : KSerializer<DateTime> {
        val dateFormat = DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

        override fun deserialize(decoder: Decoder): DateTime {
            val dateTimeString = decoder.decodeString()
            return DateTime(dateFormat.parse(dateTimeString))
        }
        override fun serialize(encoder: Encoder, value: DateTime) {
            val dateTimeString = dateFormat.format(value.dateTime)
            encoder.encodeString(dateTimeString)
        }
    }
}

@Serializable
data class JourneyOption(
    val journeyOptionToken: String,
    val journeyId: String,
    val originStation: JourneyOptionStation,
    val destinationStation: JourneyOptionStation,
    val departureTime: DateTime,
    val arrivalTime: DateTime,
    val departureRealTime: DateTime,
    val arrivalRealTime: DateTime,
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