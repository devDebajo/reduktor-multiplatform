package ru.debajo.reduktor.demo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AndroidComposeEntryPoint(modifier: Modifier = Modifier) {
    ComposeEntryPoint(modifier.fillMaxSize())
}
