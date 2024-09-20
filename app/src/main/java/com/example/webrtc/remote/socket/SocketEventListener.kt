package com.example.webrtc.remote.socket

import com.example.webrtc.data.MessageModel

interface SocketEventListener {

    fun onNewMessage(message: MessageModel)

    fun onSocketOpened()

    fun onSocketClosed()
}