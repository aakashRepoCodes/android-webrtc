
import com.example.webrtc.remote.socket.SocketEvents.*
import com.example.webrtc.WebRTCApplication
import com.example.webrtc.data.MessageModel
import com.example.webrtc.remote.socket.SocketClient
import javax.inject.Inject

class SocketEventSender @Inject constructor(
    private val socketClient: SocketClient
) {

    private var username = WebRTCApplication.username

    fun storeUser() {
        socketClient.sendMessageToSocket(
            MessageModel(type = StoreUser, name = username)
        )
    }

    fun createRoom(roomName: String) {
        socketClient.sendMessageToSocket(
            MessageModel(type = CreateRoom, data = roomName, name = username)
        )
    }

    fun joinRoom(roomName: String) {
        socketClient.sendMessageToSocket(
            MessageModel(type = JoinRoom, data = roomName, name = username)
        )
    }

    fun leaveAllRooms() {
        socketClient.sendMessageToSocket(
            MessageModel(type = LeaveAllRooms, name = username)
        )
    }

    fun startCall(target: String) {
        socketClient.sendMessageToSocket(
            MessageModel(type = StartCall, name = username, target = target)
        )
    }
}