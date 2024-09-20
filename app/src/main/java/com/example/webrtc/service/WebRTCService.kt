package com.example.webrtc.service

import SocketEventSender
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.compose.runtime.internal.composableLambdaInstance
import androidx.core.app.NotificationCompat
import com.example.webrtc.MainActivity
import com.example.webrtc.R
import com.example.webrtc.data.MessageModel
import com.example.webrtc.remote.socket.SocketClient
import com.example.webrtc.remote.socket.SocketEventListener
import dagger.hilt.android.AndroidEntryPoint
import org.webrtc.RTCStats
import javax.inject.Inject

@AndroidEntryPoint
class WebRTCService: Service(), SocketEventListener {

    @Inject
    lateinit var socketClient: SocketClient

    //@Inject
    lateinit var eventSender: SocketEventSender

    private val binder: LocalBinder = LocalBinder()

    //service section
    private lateinit var mainNotification: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager

    inner class LocalBinder : Binder(){
        fun getService(): WebRTCService = this@WebRTCService
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(
            NotificationManager::class.java
        )

        createNotifications()
        socketClient.setEventListener(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when(it.action){
                ServiceActions.START.toString() -> handleStartService()

                ServiceActions.STOP.toString() -> handleStopService()

                else -> Unit
            }
        }
        return START_STICKY
    }

    private fun handleStopService() {
        if (isServiceRunning) {
            isServiceRunning = false
            socketClient.onStop()


            stopForeground(STOP_FOREGROUND_REMOVE)
        }
    }

    private fun handleStartService() {
        startForeground(MAIN_NOTIFICATION_ID,
            mainNotification.build())
    }

    private fun createNotifications() {
        val callChannel = NotificationChannel(
            CALL_NOTIFICATION_CHANNEL_ID,
            CALL_NOTIFICATION_CHANNEL_ID,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(callChannel)
        val contentIntent = Intent(
            this, MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE
        )


        val notificationChannel = NotificationChannel(
            "chanel_terminal_bluetooth",
            "chanel_terminal_bluetooth",
            NotificationManager.IMPORTANCE_HIGH
        )


        val intent = Intent(this, RTCBroadcastReciever::class.java).apply {
            action = "ACTION_EXIT"
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        notificationManager.createNotificationChannel(notificationChannel)
        mainNotification = NotificationCompat.Builder(
            this, "chanel_terminal_bluetooth"
        ).setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(false)
            .addAction(R.mipmap.ic_launcher, "Exit", pendingIntent)
            .setContentIntent(contentPendingIntent)
    }

    override fun onNewMessage(message: MessageModel) {
       // eventSender.storeUser()
    }

    override fun onSocketOpened() {

    }

    override fun onSocketClosed() {

    }

    companion object {
        var isServiceRunning = false
        const val CALL_NOTIFICATION_CHANNEL_ID = "CALL_CHANNEL"
        const val MAIN_NOTIFICATION_ID = 2323



        fun startService(context: Context) {
            Thread {
                val intent = Intent(context, WebRTCService::class.java).apply {
                    action = ServiceActions.START.name
                }
                context.startForegroundService(intent)
            }.start()
        }

        fun stopService(context: Context) {
            val intent = Intent(context, WebRTCService::class.java).apply {
                action = ServiceActions.STOP.name
            }
            context.startForegroundService(intent)
        }
    }

}