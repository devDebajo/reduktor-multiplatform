package ru.debajo.reduktor.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
internal fun ComposeEntryPoint(modifier: Modifier = Modifier) {
    val store = remember { createStore() }
    val state by store.state.collectAsState()

    Box(modifier) {
        Text("Reduktor kmm: counter ${state.count}")
    }
}

fun createStore(): ReduktorStore<State, Event, Unit> {
    return reduktorStore(
        initialState = State(),
        initialEvents = listOf(Event.Start),
        eventReduktor = MReduktor(),
        commandProcessors = listOf(MCommandProcessor()),
        commandResultReduktor = MResultReduktor(),
    )
}

data class State(val count: Int = 0)

sealed interface Event {
    object Start : Event
}

class MReduktor : Reduktor<State, Event, Unit> {
    override fun invoke(state: State, event: Event): Akt<State, Unit> {
        return Akt()
    }
}

class MResultReduktor : Reduktor<State, CommandResult, Unit> {
    override fun invoke(state: State, event: CommandResult): Akt<State, Unit> {
        return when (event) {
            is MCommandResult -> Akt(state.copy(count = event.counter))
            else -> Akt()
        }
    }
}

class MCommandProcessor : CommandProcessor {
    override fun invoke(commands: Flow<Command>): Flow<CommandResult> {
        return flow {
            var counter = 0
            while (true) {
                delay(1000)
                emit(MCommandResult(counter++))
            }
        }
    }
}

class MCommandResult(val counter: Int) : CommandResult
