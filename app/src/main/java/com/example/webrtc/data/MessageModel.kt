package com.example.webrtc.data

import com.example.webrtc.remote.socket.SocketEvents

data class MessageModel(
    val type: SocketEvents,
    val name: String? = null,
    val target: String? = null,
    val data: Any? = null
)