package ru.debajo.reduktor.demo

actual object Logger {
    actual fun e(throwable: Throwable) {
        throwable.printStackTrace()
    }

    actual fun d(message: String) {
        println("KMM Logger $message")
    }
}