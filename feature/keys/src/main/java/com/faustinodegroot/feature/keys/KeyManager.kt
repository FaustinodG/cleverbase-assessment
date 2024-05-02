package com.faustinodegroot.feature.keys

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.cert.Certificate

class KeyManager {

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateKeys(identifier: String): KeyPair? {
        try {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            if (!keyStore.containsAlias(identifier)) {
                val keyPairGenerator =
                    KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")

                val parameterSpec = KeyGenParameterSpec.Builder(
                    identifier,
                    KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY or KeyProperties.PURPOSE_DECRYPT
                ).setKeySize(2048)
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    .setUserAuthenticationRequired(false)
                    .build()

                keyPairGenerator.initialize(parameterSpec)
                return keyPairGenerator.generateKeyPair()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getPrivateKey(identifier: String): PrivateKey? {
        return getKeyPair(identifier)?.private
    }

    fun getPublicKey(identifier: String): PublicKey? {
        return getKeyPair(identifier)?.public
    }

    private fun getKeyPair(identifier: String): KeyPair? {
        try {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)

            val certificate: Certificate? = keyStore.getCertificate(identifier)
            val publicKey: PublicKey? = certificate?.publicKey

            val privateKey: PrivateKey? = keyStore.getKey(identifier, null) as? PrivateKey

            if (publicKey != null && privateKey != null) {
                return KeyPair(publicKey, privateKey)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}