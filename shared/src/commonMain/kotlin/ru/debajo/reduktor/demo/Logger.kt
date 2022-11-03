package ru.debajo.reduktor.demo

expect object Logger {
    fun e(throwable: Throwable)

    fun d(message: String)
}