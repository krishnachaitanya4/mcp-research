package server

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.streams.*
import io.modelcontextprotocol.kotlin.sdk.*
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.io.asSink
import kotlinx.io.buffered
import kotlinx.serialization.json.*
import sun.net.www.protocol.http.AuthCacheValue
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun `run mcp server`() {


    // Create the MCP Server instance with a basic implementation
    val server = AuthCacheValue.Type.Server(
        Implementation(
            name = "Buyleads", // Tool name is "weather"
            version = "1.0.0" // Version of the implementation
        ),
        ServerOptions(
            capabilities = ServerCapabilities(tools = ServerCapabilities.Tools(listChanged = true))
        )
    )

    server.addTool(
        name = "get_buyleads",
        description = "Fetch buyleads from the OLX CTE API using a hardcoded dealer and device ID.",
        inputSchema = Tool.Input(properties = buildJsonObject { }, required = emptyList())
    ) {
        val messageContent = runBlocking {
            val cteClient = HttpClient {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                    })
                }
            }

            val buyleads = cteClient.getBuyLeads()
            val logTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

            buyleads.map { lead ->
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
                CV Date: ${lead.cvdate}
                Cars: ${
                    lead.cars?.joinToString("\n") {
                        "${it?.make} ${it?.model} (${it?.year})"
                    } ?: "None"
                }
            """.trimIndent()

                File("tool_logs.txt").appendText("[$logTime] get_buyleads:\n$text\n\n")
                TextContent(text)
            }
        }

        CallToolResult(content = messageContent)
    }


    server.addTool(
        name = "update_buylead",
        description = """
Update a buylead on the OLX CTE API. Use only lowercase keys exactly as shown below.

Example:
{
  "status_date": "2025-04-26",
  "city": "Achanta",
  "substatus": "Call Later",
  "mobile": "9898872311",
  "statustext": "gdhdhhd",
  "buylead_id": "829",
  "status_category": "HOT",
  "name": "bike Pradesh",
  "state": "Andhra Pradesh",
  "email": "",
  "status": "Followup",
}

You can send empty string if you wish not to change.
 
""".trimIndent(),
        inputSchema = Tool.Input(
            properties = buildJsonObject {
                putJsonObject("data") {
                    put("type", "object")
                    put("description", "Flat object with buylead_id, name, city, mobile, etc.")
                    put("additionalProperties", JsonPrimitive(true))
                }
            },
            required = listOf("data")
        )
    ) { request ->
        val rawData = request.arguments["data"]?.jsonObject?.mapValues { it.value.jsonPrimitive.content }

        if (rawData == null || !rawData.keys.any { it.lowercase() == "buylead_id" }) {
            return@addTool CallToolResult(
                content = listOf(TextContent("The 'data' must include 'buylead_id'."))
            )
        }

        fun normalizeKeys(input: Map<String, String>): Map<String, String> {
            val allowedKeys = setOf(
                "buylead_id", "status_date", "city", "substatus", "mobile", "statustext",
                "status_category", "name", "state", "email", "status"
            )

            return input.mapKeys { (key, _) ->
                key.lowercase().replace(" ", "_")
            }.filterKeys { it in allowedKeys }
        }

        val normalizedData = normalizeKeys(rawData)

        val responseText = runBlocking {
            val client = HttpClient {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                    })
                }
            }

            client.use {
                it.updateBuylead(normalizedData)
            }
        }

        CallToolResult(content = listOf(TextContent("Update response: $responseText")))
    }





    // Create a transport using standard IO for server communication
    val transport = StdioServerTransport(
        System.`in`.asInput(),
        System.out.asSink().buffered()
    )

    runBlocking {
        server.connect(transport)
        val done = Job()
        server.onClose {
            done.complete()
        }
        done.join()
    }
}