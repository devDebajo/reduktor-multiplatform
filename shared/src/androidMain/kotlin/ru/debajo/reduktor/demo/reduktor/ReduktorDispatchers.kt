package ru.debajo.reduktor.demo.reduktor

import java.util.concurrent.Executors
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher

actual object ReduktorDispatchers {
    actual val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    actual val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO

    actual val singleThreadDispatcher: CoroutineDispatcher
        get() = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    actual val defaultDispatcher: CoroutineDispatcher
        get() = Dispatchers.Default
}
