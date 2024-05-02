package com.faustinodegroot.secretmessaging

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.faustinodegroot.feature.keys.KeyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoView(isEncryption: Boolean, keyViewModel: KeyViewModel = hiltViewModel()) {

    val allKeys by keyViewModel.allKeys.collectAsState()
    var inputMessage by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    var selectedKey by remember { mutableStateOf(Pair("", "")) }
    val convertedMessage by keyViewModel.convertedMessage.collectAsState()
    val isVerified by keyViewModel.isVerified.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        TextField(
            value = inputMessage,
            onValueChange = { inputMessage = it },
            label = { Text("Fill in message") },
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            minLines = 11,
            maxLines = 11
        )

        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(),
            expanded = isExpanded,
            onExpandedChange = {
                isExpanded = !isExpanded
            }
        ) {
            TextField(
                readOnly = true,
                value = selectedKey.first,
                onValueChange = { },
                label = { Text("Select user") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = isExpanded
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = {
                    isExpanded = false
                }
            ) {
                allKeys?.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption.key) },
                        onClick = {
                            selectedKey = Pair(selectionOption.key, selectionOption.value)
                            isExpanded = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                if (isEncryption) {
                    keyViewModel.encryptMessage(selectedKey.first, selectedKey.second, inputMessage)
                } else {
                    keyViewModel.decryptMessage(selectedKey.first, selectedKey.second, inputMessage)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(text = if (isEncryption) "Encrypt message" else "Decrypt message")
        }

        BasicTextField(
            value = convertedMessage,
            onValueChange = { },
            readOnly = true,
            visualTransformation = VisualTransformation.None
        )

        if(!isEncryption) {
            Text(
                text = if (isVerified)
                    "Decrypted message is verified and not tampered with"
                else if (inputMessage.isNotEmpty())
                    "Message is not verified" else "",
                color = if (isVerified) Color.Green else Color.Red
            )
        }
    }
}