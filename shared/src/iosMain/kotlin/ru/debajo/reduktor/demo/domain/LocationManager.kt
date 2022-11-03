package ru.debajo.reduktor.demo.domain

private class IosLocationManager : LocationManager {
    override fun isLocationAllowed(): Boolean = true

    override suspend fun getCurrentLocation(): Pair<Double, Double> = 0.0 to 0.0
}

actual fun LocationManager(): LocationManager {
    return IosLocationManager()
}
