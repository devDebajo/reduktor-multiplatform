package ru.debajo.reduktor.demo.reduktor

import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.freeze
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.newFixedThreadPoolContext
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue

actual object ReduktorDispatchers {

    actual val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    actual val ioDispatcher: CoroutineDispatcher
        get() = IODispatcher

    actual val singleThreadDispatcher: CoroutineDispatcher
        get() = newFixedThreadPoolContext(1, "Single thread Dispatcher")

    actual val defaultDispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    private object IODispatcher : CoroutineDispatcher() {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0.toULong())) {
                try {
                    block.run().freeze()
                } catch (err: Throwable) {
                    throw err
                }
            }
        }
    }
}
