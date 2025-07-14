package server

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import server.data.*


suspend fun HttpClient.getBuyLeads(): List<BuyleadItem> {
    val uri = "https://testolxapi4.cartradeexchange.com/mobile_api"
    val response = this.post(uri) {
        contentType(ContentType.Application.Json)
        setBody(
            mapOf(
                "action" to "loadallbuylead",
                "dealer_id" to "400806820",
                "device_id" to "4fee41be780ae0e7",
                "api_id" to "cteolx2024v1.0"
            )
        )
    }.body<Response>()
    return response.data?.buyleads?.buylead?.filterNotNull() ?: emptyList()
}
/*
* "buylead_id":808,
  "state":"Andhra Pradesh",
  "name":"Dharmesh",
  "mobile":"9714427293",
  "city":"Kakinada"*/
suspend fun HttpClient.updateBuylead(data: Map<String, String>): String{
    val uri = "https://testolxapi4.cartradeexchange.com/mobile_api"
    val map = mapOf(
        "action" to "updatebuylead",
        "dealer_id" to "400806820",
        "device_id" to "4fee41be780ae0e7",
        "api_id" to "cteolx2024v1.0",
    )
    val dynamicMap: Map<String, String> = map + data
    val response = this.post(uri) {
        contentType(ContentType.Application.Json)
        setBody(dynamicMap)
    }.body<UpdateResponse>()
    val buyleads = response.data ?: response.error ?: ""
    return buyleads
}




