package ru.debajo.reduktor

import java.util.concurrent.Executors
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher

actual object ReduktorDispatchers {
    actual val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    actual val mainDispatcher: CoroutineDispatcher = Dispatchers.Main

    actual val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    actual val singleThreadDispatcher: CoroutineDispatcher
        get() = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
}
