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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
fun AndroidComposeEntryPoint(modifier: Modifier = Modifier) {
    CompositionLocalProvider(
        LocalContentColor provides if (isSystemInDarkTheme()) Color.White else Color.Black
    ) {
        MaterialTheme {
            PermissionsRequired(
                permissions = listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
                permissionsNotGrantedContent = { Text("No location permission") },
                permissionsGrantedContent = { ComposeEntryPoint(modifier.fillMaxSize()) }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun PermissionsRequired(
    permissions: List<String>,
    permissionsNotGrantedContent: @Composable () -> Unit,
    permissionsGrantedContent: @Composable () -> Unit,
) {
    val permissionsGranted = remember { mutableStateOf(false) }
    val permissionState = rememberMultiplePermissionsState(
        permissions = permissions,
        onPermissionsResult = { result ->
            permissionsGranted.value = permissions.all { result[it] == true }
        }
    )
    LaunchedEffect(key1 = permissionState, block = {
        permissionState.launchMultiplePermissionRequest()
    })

    if (permissionsGranted.value) {
        permissionsGrantedContent()
    } else {
        permissionsNotGrantedContent()
    }
}
