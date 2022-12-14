package ru.debajo.reduktor

import kotlinx.coroutines.CoroutineDispatcher

expect object ReduktorDispatchers {
    val defaultDispatcher: CoroutineDispatcher
    val mainDispatcher: CoroutineDispatcher
    val ioDispatcher: CoroutineDispatcher
    val singleThreadDispatcher: CoroutineDispatcher
}
