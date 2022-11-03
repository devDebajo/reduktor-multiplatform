package ru.debajo.reduktor.demo.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import kotlinx.serialization.json.Json
import ru.debajo.reduktor.demo.data.WeatherService
import ru.debajo.reduktor.demo.data.WeatherServiceImpl
import ru.debajo.reduktor.demo.domain.GetWeatherUseCase
import ru.debajo.reduktor.demo.domain.LocationManager
import ru.debajo.reduktor.demo.domain.processor.LoadCurrentWeatherCommandProcessor
import ru.debajo.reduktor.demo.domain.processor.LoadLocationCommandProcessor

object Di {

    private val json: Json by lazy {
        Json { ignoreUnknownKeys = true }
    }

    private val httpClient: HttpClient by lazy {
        HttpClient {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                      ru.debajo.reduktor.demo.Logger.d(message)
                    }
                }
            }
        }
    }

    private fun provideWeatherService(): WeatherService {
        return WeatherServiceImpl(httpClient, json)
    }

    private fun provideGetWeatherUseCase(): GetWeatherUseCase {
        return GetWeatherUseCase(provideWeatherService())
    }

    private fun provideLocationManager(): LocationManager = LocationManager()

    fun provideLoadCurrentWeatherCommandProcessor(
        getWeatherUseCase: GetWeatherUseCase = provideGetWeatherUseCase()
    ): LoadCurrentWeatherCommandProcessor = LoadCurrentWeatherCommandProcessor(getWeatherUseCase)

    fun provideLoadLocationCommandProcessor(): LoadLocationCommandProcessor {
        return LoadLocationCommandProcessor(provideLocationManager())
    }
}
