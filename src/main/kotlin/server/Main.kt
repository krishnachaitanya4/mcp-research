package server

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.modelcontextprotocol.kotlin.sdk.TextContent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main() = `run mcp server`()
/*
fun main() = runBlocking {
    HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }.use { cteClient ->

        val buyleads = cteClient.getBuyLeads()
        println(buyleads.toString())
        val res = buyleads.map { lead ->
            val text = """
                Buylead ID: ${lead.buylead_id}
                Name: ${lead.name}
                Mobile: ${lead.mobile}
                Email: ${lead.email}
                Date: ${lead.date}
                Status: ${lead.status}
                Status Text: ${lead.statustext}
                Status Date: ${lead.status_date}
                Contact Name: ${lead.contact_name}
                Customer Visited: ${lead.customer_visited}
                Substatus: ${lead.substatus}
                Mobile Clicked: ${lead.mobileClicked}
                Verified Lead: ${lead.verifiedLead}
                Added Date: ${lead.addeddate}
                Status Banner: ${lead.statusBanner}
                Status Category: ${lead.status_category}
                OLX Buyer ID: ${lead.olx_buyer_id}
                Cars: ${
                lead.cars?.joinToString("\n") {
                    "${it?.make} ${it?.model} (${it?.year})"
                } ?: "None"
            }
            """.trimIndent()

            println(text)
        }


    }
}
*/
