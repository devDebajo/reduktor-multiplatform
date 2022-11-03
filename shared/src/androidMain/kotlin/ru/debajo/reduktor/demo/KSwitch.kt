package ru.debajo.reduktor.demo

import androidx.compose.material.Switch
import androidx.compose.material.SwitchColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun KSwitch(
    modifier: Modifier,
    checked: Boolean,
    colors: SwitchColors,
    onCheckedChange: (Boolean) -> Unit,
) {
    Switch(
        modifier = modifier,
        checked = checked,
        colors = colors,
        onCheckedChange = onCheckedChange,
    )
}
