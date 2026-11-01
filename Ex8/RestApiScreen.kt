package com.chris.neo.ex8

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun RestApiAppScreen() {
    val coroutineScope = rememberCoroutineScope()
    var responseText by remember { mutableStateOf("Click the button below to fetch data from REST API.") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "REST API Data Fetcher",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                isLoading = true
                responseText = "Fetching data..."
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        val url = URL("https://jsonplaceholder.typicode.com/posts/1")
                        val connection = url.openConnection() as HttpURLConnection
                        connection.requestMethod = "GET"
                        connection.connectTimeout = 5000
                        connection.readTimeout = 5000

                        val responseCode = connection.responseCode
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            val jsonRaw = connection.inputStream.bufferedReader().use { it.readText() }
                            val jsonObject = JSONObject(jsonRaw)

                            val userId = jsonObject.optInt("userId")
                            val id = jsonObject.optInt("id")
                            val title = jsonObject.optString("title")
                            val body = jsonObject.optString("body")

                            val formattedText = """
                                Response Status: HTTP 200 OK
                                
                                --- Parsed JSON Data ---
                                ID: $id
                                User ID: $userId
                                Title: $title
                                
                                Body:
                                $body
                                
                                --- Raw JSON ---
                                $jsonRaw
                            """.trimIndent()

                            withContext(Dispatchers.Main) {
                                responseText = formattedText
                                isLoading = false
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                responseText = "HTTP Error Code: $responseCode"
                                isLoading = false
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            responseText = "Error fetching data: ${e.localizedMessage ?: "Unknown error"}"
                            isLoading = false
                        }
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(if (isLoading) "Fetching..." else "Fetch REST API Data")
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = responseText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}