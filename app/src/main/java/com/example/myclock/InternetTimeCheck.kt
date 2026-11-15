package com.example.myclock


import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun InternetTimeCheck() {
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf("") }

    Button(enabled = !loading, onClick = {
        loading = true
        result = "Sedang menyemak masa…"

        scope.launch {
            try {
                val time = withContext(Dispatchers.IO) {
                    val url = URL(
                        "https://timeapi.io/api/Time/current/zone?timeZone=Asia%2FKuala_Lumpur"
                    )
                    val connection = url.openConnection() as HttpURLConnection
                    try {
                        connection.connectTimeout = 5000
                        connection.readTimeout = 5000
                        val response = connection.inputStream.bufferedReader().use { it.readText() }
                        JSONObject(response).getString("time")
                    } finally {
                        connection.disconnect()
                    }
                }
                result = "Masa Kuala Lumpur: $time (TimeAPI.io)"
            } catch (e: IOException) {
                result = "Semakan gagal. Cuba lagi apabila internet tersedia."
            } catch (e: JSONException) {
                result = "Respons daripada TimeAPI.io tidak dapat dibaca."
            } finally {
                loading = false
            }
        }
    }) {
        Text("Semak Masa Internet")
    }

    if (result.isNotEmpty()) Text(result)
}