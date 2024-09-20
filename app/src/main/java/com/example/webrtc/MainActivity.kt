package com.example.webrtc

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.webrtc.ui.theme.WebRTCTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.contracts.contract

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WebRTCTheme {
                 Surface (
                     modifier = Modifier.fillMaxSize(),
                     color = MaterialTheme.colorScheme.background
                 ) {
                     val permissionLauncher = rememberLauncherForActivityResult(
                         contract = ActivityResultContracts.RequestMultiplePermissions()
                     ) {

                     }

                     LaunchedEffect(key1 = Unit) {
                         permissionLauncher.launch(
                             arrayOf(
                                 Manifest.permission.RECORD_AUDIO,
                                 Manifest.permission.CAMERA,
                                 Manifest.permission.POST_NOTIFICATIONS
                             )
                         )
                     }
                 }
            }
        }
    }
}
