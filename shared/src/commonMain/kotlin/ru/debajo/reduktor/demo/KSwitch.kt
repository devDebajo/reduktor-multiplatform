package ru.debajo.reduktor.demo

import androidx.compose.material.SwitchColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun KSwitch(
    modifier: Modifier,
    checked: Boolean,
    colors: SwitchColors,
    onCheckedChange: (Boolean) -> Unit,
)
