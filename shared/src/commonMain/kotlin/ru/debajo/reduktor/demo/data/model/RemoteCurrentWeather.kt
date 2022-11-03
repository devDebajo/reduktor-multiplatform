package ru.debajo.reduktor.demo.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCurrentWeather(
    @SerialName("daily")
    val daily: Daily,
) {
    @Serializable
    data class Daily(
        @SerialName("time")
        val dates: List<String>,

        @SerialName("temperature_2m_max")
        val temperatureMax: List<Float>,

        @SerialName("temperature_2m_min")
        val temperatureMin: List<Float>,

        @SerialName("apparent_temperature_max")
        val feelsLikeMax: List<Float>,

        @SerialName("apparent_temperature_min")
        val feelsLikeMin: List<Float>,
    )
}
