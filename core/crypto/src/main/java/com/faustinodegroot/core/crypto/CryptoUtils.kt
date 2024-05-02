package com.faustinodegroot.core.crypto

import android.util.Base64
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

class CryptoUtils {

    /**
     * Encrypts a message using a public key
     */
    fun encryptMessage(message: String, encodedKey: String): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, convertKey(encodedKey))
        val encryptedBytes = cipher.doFinal(message.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    /**
     * Encrypts a message using a private key
     */
    fun signMessage(encryptedMessage: String, privateKey: PrivateKey): String {
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initSign(privateKey)
        signature.update(encryptedMessage.toByteArray())
        val signatureBytes = signature.sign()
        return Base64.encodeToString(signatureBytes, Base64.DEFAULT)
    }

    /**
     * Converts an encoded public key to a `PublicKey` object
     */
    fun convertKey(encodedKey: String): PublicKey {
        val keyBytes = Base64.decode(encodedKey, Base64.DEFAULT)
        val spec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(spec)
    }

    /**
     * Verifies the signature of a message using the public key.
     */
    fun verify(originalMessage: String, signature: String, publicKey: PublicKey): Boolean {
        val signatureInstance = Signature.getInstance("SHA256withRSA")
        signatureInstance.initVerify(publicKey)
        signatureInstance.update(originalMessage.toByteArray())
        val signatureBytes = Base64.decode(signature, Base64.DEFAULT)
        return signatureInstance.verify(signatureBytes)
    }

    /**
     * Decrypts a message using a private key
     */
    fun decryptMessage(encryptedMessage: String, privateKey: PrivateKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decryptedBytes = cipher.doFinal(Base64.decode(encryptedMessage, Base64.DEFAULT))
        return String(decryptedBytes)
    }

}