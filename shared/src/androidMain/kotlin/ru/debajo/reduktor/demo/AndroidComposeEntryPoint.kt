package ru.debajo.reduktor.demo

import android.Manifest
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun AndroidComposeEntryPoint(modifier: Modifier = Modifier) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )
    LaunchedEffect(key1 = permissionState, block = {
        permissionState.launchMultiplePermissionRequest()
    })
    CompositionLocalProvider(
        LocalContentColor provides if (isSystemInDarkTheme()) Color.White else Color.Black
    ) {
        MaterialTheme {
            PermissionsRequired(
                multiplePermissionsState = permissionState,
                permissionsNotGrantedContent = { Text("No location permission") },
                permissionsNotAvailableContent = { Text("No location available") },
                content = {
                    ComposeEntryPoint(modifier.fillMaxSize())
                }
            )
        }
    }
}
