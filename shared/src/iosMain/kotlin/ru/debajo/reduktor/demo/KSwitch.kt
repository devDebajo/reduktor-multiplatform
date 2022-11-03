package ru.debajo.reduktor.demo

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SwitchColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal actual fun KSwitch(
    modifier: Modifier,
    checked: Boolean,
    colors: SwitchColors,
    onCheckedChange: (Boolean) -> Unit,
) {
    val checkedBgColor by colors.trackColor(enabled = true, checked = true)
    val uncheckedBgColor by colors.trackColor(enabled = true, checked = false)
    val checkedThumbColor by colors.thumbColor(enabled = true, checked = true)
    val uncheckedThumbColor by colors.thumbColor(enabled = true, checked = false)

    val bgAnimator = remember { Animatable(uncheckedBgColor) }
    val thumbAnimator = remember { Animatable(uncheckedThumbColor) }
    val thumbOffsetAnimator = remember { Animatable(0f) }
    LaunchedEffect(key1 = checkedBgColor, key2 = checked, block = {
        if (checked) {
            bgAnimator.launchAnimateTo(this, checkedBgColor)
            thumbAnimator.launchAnimateTo(this, checkedThumbColor)
            thumbOffsetAnimator.launchAnimateTo(this, 20f, 300)
        } else {
            bgAnimator.launchAnimateTo(this, uncheckedBgColor)
            thumbAnimator.launchAnimateTo(this, uncheckedThumbColor)
            thumbOffsetAnimator.launchAnimateTo(this, 0f, 300)
        }
    })

    Box(
        modifier
            .size(51.dp, 31.dp)
            .clip(RoundedCornerShape(50))
            .background(bgAnimator.value)
            .clickable { onCheckedChange(!checked) }
    ) {
        Box(
            Modifier
                .offset(thumbOffsetAnimator.value.dp)
                .padding(2.dp)
                .size(27.dp)
                .clip(RoundedCornerShape(50))
                .background(thumbAnimator.value)
        )
    }
}

private fun <T, V : AnimationVector> Animatable<T, V>.launchAnimateTo(
    scope: CoroutineScope,
    targetValue: T,
    durationMs: Int = 500
) {
    scope.launch { animateTo(targetValue, tween(durationMs)) }
}
