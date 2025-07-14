package server.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
data class Response(
    val data: Data? = null,
    val details: String? = null,
    val error: String? = null,
    val status: String? = null
)

@Serializable
data class UpdateResponse(
    val data: String? = null,
    val details: String? = null,
    val error: String? = null,
    val status: String? = null
)

@Serializable
data class Pagination(
    val next: Int? = null,
    val previous: Int? = null,
    val firstpage: Int? = null,
    val spage: Int? = null,
    val lastpage: Int? = null,
    val displaying: String? = null
)

@Serializable
data class Data(
    val buyleads: Buyleads? = null
)

@Serializable
data class FiltersCount(
    val all: String? = null,
    val followups: String? = null,
    val booked: String? = null,
    val followupPending: String? = null,
    val converted: String? = null,
    val followupToday: String? = null,
    val closed: String? = null,
    val fresh: String? = null
)

@Serializable
data class StatusCountItem(
    val name: String? = null,
    val count: Int? = null
)

@Serializable
data class BuyleadItem(
    val date: String? = null,
    val status_date: String? = null,
    val contact_name: String? = null,
    val customer_visited: String? = null,
    val substatus: String? = null,
    val mobile: String? = null,
    val mobileClicked: String? = null,
    val statustext: String? = null,
    val buylead_id: String? = null,
    val verifiedLead: String? = null,
    val cars: List<CarsItem?>? = null,
    val addeddate: String? = null,
    val statusBanner: String? = null,
    val status_category: String? = null,
    val olx_buyer_id: Int? = null,
    val name: String? = null,
    val cvdate: String? = null,
    val email: String? = null,
    val status: String? = null
)

@Serializable
data class Buyleads(
    val filtersCount: FiltersCount? = null,
    val pagination: Pagination? = null,
    val buylead: List<BuyleadItem?>? = null,
    val statusCount: List<StatusCountItem?>? = null,
    val filtersCountIos: List<JsonElement?>? = null
)

@Serializable
data class CarsItem(
    val adId: String? = null,
    val year: String? = null,
    val model: String? = null,
    val id: String? = null,
    val make: String? = null
)
@Serializable
data class FormattedBuyLead(
    val name: String,
    val mobile: String,
    val email: String,
    val date: String,
    val status: String,
    val statusText: String,
    val cars: String
)

// Data class representing the points response from the API
@Serializable
data class Points(
    val properties: Properties
) {
    @Serializable
    data class Properties(val forecast: String)
}

// Data class representing the forecast response from the API
@Serializable
data class Forecast(
    val properties: Properties
) {
    @Serializable
    data class Properties(val periods: List<Period>)

    @Serializable
    data class Period(
        val number: Int, val name: String, val startTime: String, val endTime: String,
        val isDaytime: Boolean, val temperature: Int, val temperatureUnit: String,
        val temperatureTrend: String, val probabilityOfPrecipitation: JsonObject,
        val windSpeed: String, val windDirection: String,
        val shortForecast: String, val detailedForecast: String,
    )
}

// Data class representing the alerts response from the API
@Serializable
data class Alert(
    val features: List<Feature>
) {
    @Serializable
    data class Feature(
        val properties: Properties
    )

    @Serializable
    data class Properties(
        val event: String, val areaDesc: String, val severity: String,
        val description: String, val instruction: String?,
    )
}