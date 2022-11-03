package ru.debajo.reduktor.demo.model

sealed interface News {
    class Error(val message: String) : News
}
