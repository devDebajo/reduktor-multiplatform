package ru.debajo.reduktor.demo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Application
import platform.UIKit.UIViewController

fun getRootController(): UIViewController = Application("iOS Application") {
    ComposeEntryPoint(Modifier.fillMaxSize())
}
