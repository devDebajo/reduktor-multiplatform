package ru.debajo.reduktor.demo.model

sealed interface Event {
    object LoadCity : Event
    object Refresh : Event
}
