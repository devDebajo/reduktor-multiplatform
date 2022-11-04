package ru.debajo.reduktor.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.debajo.reduktor.demo.di.Di
import ru.debajo.reduktor.demo.model.Event
import ru.debajo.reduktor.demo.model.News
import ru.debajo.reduktor.demo.model.State
import ru.debajo.reduktor.demo.reduktor.ReduktorStore
import ru.debajo.reduktor.demo.reduktor.reduktorStore

@Composable
internal fun ComposeEntryPoint(modifier: Modifier = Modifier) {
    val store = remember { createStore() }

    Column(modifier) {
        LaunchedEffect(key1 = store, block = {
            store.onEvent(Event.LoadCity)
        })
        val state by store.state.collectAsState()
        state.let {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                when (it) {
                    is State.Data -> {
                        Column {
                            Text("Temp: ${it.temp}")
                            Text("Feels like: ${it.feelsLike}")
                            Button(onClick = { store.onEvent(Event.Refresh) }) {
                                Text("Refresh")
                            }
                        }
                    }
                    is State.LoadingWeather -> Text("Loading")
                    is State.NoLocation -> Text("No location")
                    is State.LoadingError -> {
                        Column {
                            Text("Error")
                            Button(onClick = { store.onEvent(Event.Refresh) }) {
                                Text("Refresh")
                            }
                        }
                    }
                }
            }
        }
    }
}

fun createStore(): ReduktorStore<State, Event, News> {
    return reduktorStore(
        initialState = State.NoLocation,
        eventReduktor = MainReduktor.EventReduktor,
        commandResultReduktor = MainReduktor.CommandResultReduktor,
        commandProcessors = listOf(
            Di.provideLoadCurrentWeatherCommandProcessor(), Di.provideLoadLocationCommandProcessor()
        ),
    )
}
