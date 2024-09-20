package com.example.webrtc

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.util.UUID

@HiltAndroidApp
class WebRTCApplication: Application() {

    companion object {
        var username = UUID.randomUUID().toString().substring(0, 6)
    }

}