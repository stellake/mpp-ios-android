package com.jetbrains.handson.mpp.mobile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

expect fun platformName(): String

@Serializable
data class Station(
    val id: Int = 0,
    val name: String = "",
    val aliases: List<String> = listOf(),
    val crs: String? = null,
    val nlc: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isGroupStation: Boolean = false
)

@Serializable
data class StationList(val stations: List<Station>)

@Serializable
data class Fares(
    val numberOfAdults: Int = 0,
    val numberOfChildren: Int = 0,
    val outboundJourneys: List<JourneyOption> = listOf(),
    val inboundJourneys: List<JourneyOption> = listOf(),
    val nextOutboundQuery: String = "",
    val previousOutboundQuery: String = "",
    val nextInboundQuery: String = "",
    val previousInboundQuery: String = "",
    val bookingMessages: BookingMessages = BookingMessages()
)

@Serializable
data class JourneyOption(
    val journeyOptionToken: String = "",
    val journeyId: String = "",
    val originStation: FareStation = FareStation(),
    val destinationStation: FareStation = FareStation(),
    val departureTime: String = "",
    val arrivalTime: String = "",
    val departureRealTime: String = "",
    val arrivalRealTime: String = "",
    val status: String = "",
    val primaryTrainOperator: TrainOperator = TrainOperator(),
//    val legs: List<LegDetails>,
    val tickets: List<TicketOption> = listOf(),
    val journeyDurationInMinutes: Int = 0,
    val isFastedJourney: Boolean = false,
    val bulletins: List<Bulletin> = listOf(),
    val stationMessages: List<StationMessage> = listOf()
)

@Serializable
@SerialName("Station")
data class FareStation(
    val displayName: String = "",
    val crs: String = "",
    val nlc: String = ""
)

@Serializable
data class BookingMessages(
    val messageCentreTitle: String = "",
    val doNotShowAgainText: String = "",
    val messages: List<BookingMessage> = listOf()
)

@Serializable
data class BookingMessage(
    val title: String = "",
    val message: String = "",
    val messageDismissText: String = "",
    val showBookingFormOnDismiss: Boolean = false
)

@Serializable
data class TrainOperator(
    val code: String = "",
    val name: String = ""
)

@Serializable
data class TicketOption(
    val ticketOptionToken: String = "",
    val name: String = "",
    val description: String = "",
    val priceInPennies: Int = 0,
    val pricingItem: PricingItem = PricingItem(),
    val ticketType: String = "",
    val ticketClass: String = "",
    val ticketCategory: String = "",
    val numberOfTickets: Int = 0,
    val ticketsRemaining: Int = 0,
    val fareId: String = "",
    val upgradeDetails: UpgradeDetails = UpgradeDetails()
)

@Serializable
data class UpgradeDetails(
    val upgradeFareId: String = "",
    val type: String = "",
    val fareDifferenceInPennies: Int = 0
)

@Serializable
data class PricingItem(
    val breakdown: List<PricingDetailBreakdown> = listOf(),
    val addOns: List<AddOn> = listOf(),
    val discounts: PricingItemDiscount = PricingItemDiscount()
)

@Serializable
data class PricingDetailBreakdown(
    val passenger: String = "",
    val ticketCount: Int = 0,
    val costInPennies: Int = 0,
    val discountDescription: String = ""
)

@Serializable
data class AddOn(
    val name: String = "",
    val count: Int = 0,
    val costInPennies: Int = 0
)

@Serializable
data class PricingItemDiscount(
    val preDiscountSubTotalInPennies: Int = 0,
    val totalValueInPennies: Int = 0,
    val breakdown: List<Discount> = listOf()
)

@Serializable
data class Discount(
    val name: String = "",
    val valueInPennies: Int = 0,
    val numberOfUnits: Int = 0,
    val discountType: String = ""
)

@Serializable
data class Bulletin(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val url: String = "",
    val severity: String = ""
)

@Serializable
data class StationMessage(
    val severity: String = "",
    val category: String = "",
    val message: String = ""
)

data class JourneyDetailsLight(val token: String, val details: String)

fun createApplicationScreenMessage(): String {
    return "Kotlin Rocks on ${platformName()}"
}

