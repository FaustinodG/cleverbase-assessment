package com.faustinodegroot.secretmessaging

import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.faustinodegroot.feature.exchange.NetworkViewModel
import com.faustinodegroot.feature.keys.KeyViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConnectingView(
    networkViewModel: NetworkViewModel = hiltViewModel(),
    keyViewModel: KeyViewModel = hiltViewModel()
) {

    val username = remember { mutableStateOf("") }
    val isEnabled = username.value.isNotEmpty()

    val connectingUser by networkViewModel.username.collectAsState()
    val publicKey by networkViewModel.publicKey.collectAsState()

    LaunchedEffect(connectingUser) {
        if (connectingUser.isNotEmpty()) {
            val keyPair = keyViewModel.generateKeys(connectingUser)
            if (keyPair != null) {
                val publicKeyBytes = keyPair.public.encoded
                val encoded = Base64.encodeToString(publicKeyBytes, Base64.DEFAULT)
                networkViewModel.sendKey(encoded)
            }
        }
    }

    LaunchedEffect(publicKey) {
        if (publicKey.isNotEmpty()) {
            keyViewModel.storePublicKey(connectingUser, publicKey)
        }
    }


    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { networkViewModel.startServer(username.value) },
            enabled = isEnabled
        ) {
            Text("Start Hosting")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                networkViewModel.connectToServer(username.value)
            },
            enabled = isEnabled
        ) {
            Text("Connect to host and share keys")
        }
    }

}