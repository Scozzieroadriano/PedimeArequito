package com.example.pedimearequito.utils

import android.util.Log
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import java.net.URISyntaxException

object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket(){

        try {
            mSocket = IO.socket("http://192.168.0.100:3000/orders/delivery")
        }catch (e: URISyntaxException){
            Log.d("ERROR", "No se pudo conectar el socket ${e.message}")
        }
    }
    @Synchronized
    fun getSocket (): Socket{
        return mSocket
    }
    @Synchronized
    fun establishConnection(){
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection(){
        mSocket.disconnect()
    }

}