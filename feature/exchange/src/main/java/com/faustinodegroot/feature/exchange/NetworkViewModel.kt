package com.faustinodegroot.feature.exchange


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.ServerSocket
import java.net.Socket
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor() : ViewModel() {

    private var serverSocket: ServerSocket? = null
    private var clientSocket: Socket? = null
    private var writer: BufferedWriter? = null
    private var reader: BufferedReader? = null

    private fun setupStreams() {
        writer = BufferedWriter(OutputStreamWriter(clientSocket!!.getOutputStream()))
        reader = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
    }

    private val DEFAULT_PORT = 12345
    private val DEFAULT_IP = "10.0.2.2"

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _publicKey = MutableStateFlow("")
    val publicKey = _publicKey.asStateFlow()


    /**
     * Initializes a server host
     * Initially receives a username, and responds back with it's own username
     * A new key is generated using the client's username as an alias
     * Waits for client to send a key
     */
    fun startServer(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            serverSocket = ServerSocket(DEFAULT_PORT)
            clientSocket = serverSocket!!.accept()

            setupStreams()

            val clientUsername = reader!!.readLine()
            println("retrieved client name: $clientUsername")
            println("Sending server name: $username")
            writer?.write(username)
            writer?.newLine()
            writer?.flush()
            _username.value = clientUsername


            val keyBuilder = StringBuilder()
            var line: String?
            try {
                while (reader!!.readLine().also { line = it } != null) {
                    if (line!!.isEmpty()) {
                        break
                    }
                    keyBuilder.append(line)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            _publicKey.value = keyBuilder.toString()
        }
    }


    /**
     * Initializes a client
     * Initially sends a username, and waits for a username response
     * A new key is generated using the server's username as an alias
     * Waits for host to send a key
     */
    fun connectToServer(clientUsername: String) {
        viewModelScope.launch(Dispatchers.IO) {
            clientSocket = Socket(DEFAULT_IP, DEFAULT_PORT)
            setupStreams()

            writer?.write(clientUsername)
            writer?.newLine()
            writer?.flush()
            val serverName = reader?.readLine()

            if (serverName != null) {
                _username.value = serverName
            }

            val keyBuilder = StringBuilder()
            var line: String?
            try {
                while (reader!!.readLine().also { line = it } != null) {
                    if (line!!.isEmpty()) {
                        break
                    }
                    keyBuilder.append(line)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            _publicKey.value = keyBuilder.toString()

        }
    }

    /**
     * Sends key to other device
     */
    fun sendKey(key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            writer?.write(key)
            writer?.newLine()
            writer?.flush()
        }
    }


    /**
     * Closes server and client sockets
     */
    private fun closeConnection() {
        try {
            clientSocket?.close()
            serverSocket?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCleared() {
        super.onCleared()
        closeConnection()
    }

}