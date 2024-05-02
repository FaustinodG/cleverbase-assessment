package com.faustinodegroot.feature.keys

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faustinodegroot.core.data.repository.PublicKeyRepository
import com.faustinodegroot.core.crypto.CryptoUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.security.KeyPair
import javax.inject.Inject

@HiltViewModel
class KeyViewModel @Inject constructor(private val publicKeyRepository: PublicKeyRepository) :
    ViewModel() {

    private val keyManager = KeyManager()
    private val cryptoUtils = CryptoUtils()

    private val _allKeys = MutableStateFlow<Map<String, String>?>(null)
    val allKeys: StateFlow<Map<String, String>?> = _allKeys

    private val _convertedMessage = MutableStateFlow("")
    val convertedMessage: StateFlow<String> = _convertedMessage

    private val _isVerified = MutableStateFlow(false)
    val isVerified: StateFlow<Boolean> = _isVerified

    init {
        loadAllPublicKeys()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateKeys(identifier: String): KeyPair? {
        return keyManager.generateKeys(identifier)
    }

    fun storePublicKey(user: String, key: String) {
        publicKeyRepository.savePublicKey(user, key)
    }

    private fun loadAllPublicKeys() {
        viewModelScope.launch {
            _allKeys.value = publicKeyRepository.getAllPublicKeys()
        }
    }

    fun encryptMessage(identifier: String, key: String, message: String) {
        val encrypted = cryptoUtils.encryptMessage(message, key)
        val signature = cryptoUtils.signMessage(encrypted, keyManager.getPrivateKey(identifier)!!)
        _convertedMessage.value = "$encrypted:$signature"

    }

    fun decryptMessage(identifier: String, key: String, encryptedMessage: String) {
        val parts = encryptedMessage.split(":")
        if (parts.size == 2) {
            val message = parts[0]
            val signature = parts[1]
            _isVerified.value =
                cryptoUtils.verify(message, signature, cryptoUtils.convertKey(key))
            if (isVerified.value) {
                val decrypted =
                    cryptoUtils.decryptMessage(message, keyManager.getPrivateKey(identifier)!!)
                _convertedMessage.value = decrypted
            }
        }
    }

}