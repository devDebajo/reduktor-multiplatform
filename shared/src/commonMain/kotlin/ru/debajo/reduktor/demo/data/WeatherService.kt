package ru.debajo.reduktor.demo.data

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import ru.debajo.reduktor.demo.data.model.RemoteCurrentWeather

interface WeatherService {
    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        daily: List<String>,
        timezone: String = "GMT"
    ): RemoteCurrentWeather
}

internal class WeatherServiceImpl(
    private val client: HttpClient,
    private val json: Json,
) : WeatherService {

    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        daily: List<String>,
        timezone: String
    ): RemoteCurrentWeather {
        val result = client.get("https://api.open-meteo.com/v1/forecast") {
            url {
                parameters.append("latitude", latitude.toString())
                parameters.append("longitude", longitude.toString())
                parameters.appendAll("daily", daily)
                parameters.append("timezone", timezone)
            }
        }.bodyAsText()
        return json.decodeFromString(RemoteCurrentWeather.serializer(), result)
    }
}
