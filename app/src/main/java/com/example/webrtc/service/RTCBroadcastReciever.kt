package com.example.webrtc.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.webrtc.MainActivity

class RTCBroadcastReciever: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.action?.let{ action ->
            if (action == "ACTION_EXIT"){
                context?.let {
                    notnullContext ->
                    notnullContext.startActivity(
                        Intent(notnullContext , MainActivity::class.java)
                            .apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                    )
                }
            }

        }
    }
}