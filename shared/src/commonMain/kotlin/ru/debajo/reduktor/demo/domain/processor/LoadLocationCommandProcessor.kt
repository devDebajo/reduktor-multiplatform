package ru.debajo.reduktor.demo.domain.processor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import ru.debajo.reduktor.Command
import ru.debajo.reduktor.CommandProcessor
import ru.debajo.reduktor.CommandResult
import ru.debajo.reduktor.demo.domain.LocationManager

class LoadLocationCommandProcessor(
    private val locationManager: LocationManager
) : CommandProcessor {

    override fun invoke(commands: Flow<Command>): Flow<CommandResult> {
        return commands
            .filterIsInstance<GetLocation>()
            .filter { locationManager.isLocationAllowed() }
            .mapLatest { runCatching { locationManager.getCurrentLocation() }.getOrNull() }
            .filterNotNull()
            .map { LocationGot(it.first, it.second) }
    }

    object GetLocation : Command

    class LocationGot(val latitude: Double, val longitude: Double) : CommandResult
}
