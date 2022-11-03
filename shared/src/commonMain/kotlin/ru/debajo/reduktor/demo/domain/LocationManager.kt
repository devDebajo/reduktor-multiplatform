package ru.debajo.reduktor.demo.domain

interface LocationManager {
    fun isLocationAllowed(): Boolean

    suspend fun getCurrentLocation(): Pair<Double, Double>
}

expect fun LocationManager(): LocationManager