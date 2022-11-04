package ru.debajo.reduktor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Implementation of [ReduktorStore]
 */
@OptIn(FlowPreview::class)
internal class ReduktorStoreImpl<State : Any, Event : Any, News : Any>(
    initialState: State,
    eventReduktor: Reduktor<State, Event, News>,
    commandResultReduktor: Reduktor<State, CommandResult, News> = { _, _ -> Akt() },
    commandProcessors: List<CommandProcessor> = emptyList(),
    initialEvents: List<Event> = emptyList(),
    errorDispatcher: (Throwable) -> Unit = {},
) : ReduktorStore<State, Event, News>, CoroutineScope by CoroutineScope(SupervisorJob() + ReduktorDispatchers.mainDispatcher) {

    private val stateFlow: MutableStateFlow<State> = MutableStateFlow(initialState)
    private val eventsFlow: MutableSharedFlow<Event> = MutableSharedFlow(replay = 1)
    private val newsFlow: MutableSharedFlow<News> = MutableSharedFlow(replay = 1)
    private val commandsFlow: MutableSharedFlow<Command> = MutableSharedFlow(replay = Int.MAX_VALUE)
    private val dispatcher: CoroutineDispatcher = ReduktorDispatchers.singleThreadDispatcher
    private val errorHandler = CoroutineExceptionHandler { _, exception -> errorDispatcher(exception) }

    override val state: StateFlow<State> = stateFlow.asStateFlow()
    override val news: Flow<News> = newsFlow.asSharedFlow()
    override val currentState: State get() = stateFlow.value

    init {
        launch(dispatcher + errorHandler) {
            eventsFlow
                .map { event -> eventReduktor(stateFlow.value, event) }
                .collect { handleAkt(it) }
        }

        launch(dispatcher + errorHandler) {
            commandProcessors
                .asFlow()
                .flatMapMerge { it.invoke(commandsFlow) }
                .flowOn(ReduktorDispatchers.ioDispatcher)
                .map { commandResultReduktor(stateFlow.value, it) }
                .collect { handleAkt(it) }
        }

        if (initialEvents.isNotEmpty()) {
            launch(ReduktorDispatchers.defaultDispatcher + errorHandler) {
                eventsFlow.emitAll(initialEvents.asFlow())
            }
        }
    }

    override fun onEvent(event: Event) {
        launch {
            eventsFlow.emit(event)
        }
    }

    override fun dispose() {
        cancel()
    }

    private suspend fun handleAkt(pass: Akt<State, News>) = with(pass) {
        if (commands.isNotEmpty()) commandsFlow.emitAll(commands.asFlow())

        if (news.isNotEmpty()) newsFlow.emitAll(news.asFlow())

        if (state != null) {
            val currentState = stateFlow.value
            if (currentState != state) {
                stateFlow.value = state
            }
        }
    }
}

fun <State : Any, Event : Any, News : Any> reduktorStore(
    initialState: State,
    eventReduktor: Reduktor<State, Event, News>,
    commandResultReduktor: Reduktor<State, CommandResult, News> = { _, _ -> Akt() },
    commandProcessors: List<CommandProcessor> = emptyList(),
    initialEvents: List<Event> = emptyList(),
    errorDispatcher: (Throwable) -> Unit = {},
): ReduktorStore<State, Event, News> {
    return ReduktorStoreImpl(
        initialState = initialState,
        eventReduktor = eventReduktor,
        commandResultReduktor = commandResultReduktor,
        commandProcessors = commandProcessors,
        initialEvents = initialEvents,
        errorDispatcher = errorDispatcher,
    )
}
