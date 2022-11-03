package ru.debajo.reduktor.demo.domain

import ru.debajo.reduktor.demo.data.WeatherService
import ru.debajo.reduktor.demo.domain.model.DomainCurrentWeather

class GetWeatherUseCase(
    private val service: WeatherService
) {
    suspend fun getWeather(latitude: Double, longitude: Double): DomainCurrentWeather {
        val weather = service.getCurrentWeather(
            latitude = latitude,
            longitude = longitude,
            daily = listOf("temperature_2m_max", "temperature_2m_min", "apparent_temperature_max", "apparent_temperature_min")
        )

        return DomainCurrentWeather(
            temp = (weather.daily.temperatureMax[0] + weather.daily.temperatureMin[1]) / 2f,
            feelsLike = (weather.daily.feelsLikeMax[0] + weather.daily.feelsLikeMin[1]) / 2f,
        )
    }
}
