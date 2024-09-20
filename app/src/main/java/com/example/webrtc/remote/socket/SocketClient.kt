package com.example.webrtc.remote.socket

import com.example.webrtc.data.MessageModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import javax.inject.Inject

class SocketClient @Inject constructor(
    private val gson: Gson
) {


    private var webSocketClient: WebSocketClient? = null
    private var socketEventListener: SocketEventListener? = null

    init {
        CoroutineScope(Dispatchers.IO).launch{
            delay(1000)
            initSocket()
        }
    }

    private fun initSocket() {
        webSocketClient = object : WebSocketClient(URI("192.168.0.106")) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                socketEventListener?.onSocketOpened()
             }

            override fun onMessage(message: String?) {
                runCatching {
                    socketEventListener?.onNewMessage(
                        gson.fromJson(message, MessageModel::class.java)
                    )
                }
             }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                socketEventListener?.onSocketClosed()
            }

            override fun onError(ex: Exception?) {
             }
        }
    }

    fun setEventListener(socketEventListener: SocketEventListener){
        this.socketEventListener = socketEventListener
    }

    fun sendMessageToSocket(message: MessageModel) {
        webSocketClient?.send(gson.toJson(message, MessageModel::class.java))

    }

    fun onStop(){
        socketEventListener = null
        runCatching {
            socketEventListener?.onSocketClosed()

        }
    }
}