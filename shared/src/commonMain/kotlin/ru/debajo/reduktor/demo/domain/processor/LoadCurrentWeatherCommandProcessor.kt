package ru.debajo.reduktor.demo.domain.processor

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import ru.debajo.reduktor.demo.domain.GetWeatherUseCase
import ru.debajo.reduktor.demo.domain.model.DomainCurrentWeather
import ru.debajo.reduktor.demo.reduktor.Command
import ru.debajo.reduktor.demo.reduktor.CommandProcessor
import ru.debajo.reduktor.demo.reduktor.CommandResult

class LoadCurrentWeatherCommandProcessor(
    private val getWeatherUseCase: GetWeatherUseCase
) : CommandProcessor {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(commands: Flow<Command>): Flow<CommandResult> {
        return commands
            .filterIsInstance<LoadWeather>()
            .mapLatest<LoadWeather, LoadWeatherResult> { (latitude, longitude) ->
                getWeatherUseCase.getWeather(latitude, longitude)
                    .let { LoadWeatherResult.Loaded(it) }
            }
            .catch {
                it.printStackTrace()
                emit(LoadWeatherResult.LoadFailed(it))
            }
    }

    data class LoadWeather(val latitude: Double, val longitude: Double) : Command

    sealed interface LoadWeatherResult : CommandResult {
        class Loaded(val weather: DomainCurrentWeather) : LoadWeatherResult
        class LoadFailed(val error: Throwable) : LoadWeatherResult
    }
}
