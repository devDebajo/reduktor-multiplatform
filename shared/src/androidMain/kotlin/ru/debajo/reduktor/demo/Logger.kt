package ru.debajo.reduktor.demo

import android.util.Log

actual object Logger {
    actual fun e(throwable: Throwable) {
        Log.e("KMM Logger", "", throwable)
    }

    actual fun d(message: String) {
        Log.d("KMM Logger", message)
    }
}
