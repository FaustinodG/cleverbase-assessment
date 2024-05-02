package com.faustinodegroot.core.crypto

import android.util.Base64
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.security.KeyPair
import java.security.KeyPairGenerator

@RunWith(RobolectricTestRunner::class)
class CryptoUtilsTest {
    private val cryptoUtils = CryptoUtils()
    private lateinit var keyPair: KeyPair


    @Before
    fun setup() {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048)
        keyPair = keyPairGenerator.genKeyPair()
    }

    @Test
    fun `message should remain the same after encrypting and decrypting`() {
        val privateKey = keyPair.private
        val publicKey = keyPair.public

        val publicKeyEncoded = Base64.encodeToString(publicKey.encoded, Base64.DEFAULT)
        val originalMessage = "encryption is cool"

        val encryptedMessage = cryptoUtils.encryptMessage(originalMessage, publicKeyEncoded)
        val decryptedMessage = cryptoUtils.decryptMessage(encryptedMessage, privateKey)

        assertEquals(originalMessage, decryptedMessage)
    }

    @Test
    fun `message should be verified after signing`() {
        val privateKey = keyPair.private
        val publicKey = keyPair.public
        val message = "This message is signed"

        val signedMessage = cryptoUtils.signMessage(message, privateKey)
        assertTrue(cryptoUtils.verify(message, signedMessage, publicKey))
    }
}