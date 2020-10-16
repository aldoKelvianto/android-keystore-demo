package com.github.androidkeystoredemo

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EncryptDecryptStringTest {

    @Test
    fun encryptDecryptSuccess() {
        // Given
        val pin = "1234"

        // When
        val pair = EncryptDecrypt.encrypt(pin.toByteArray())
        val cipherBytes = pair.first
        val iv = pair.second
        val decryptedBytes = EncryptDecrypt.decrypt(cipherBytes, iv)
        val decryptedPin = String(decryptedBytes)

        // Then
        assertEquals(decryptedPin, pin)
    }

    @Test
    fun encryptDecryptFail() {
        // Given
        val pin = "1234"
        val wrongPinBytes = "12345"

        // When
        val pair = EncryptDecrypt.encrypt(pin.toByteArray())
        val cipherBytes = pair.first
        val iv = pair.second
        val decryptedBytes = EncryptDecrypt.decrypt(cipherBytes, iv)
        val decryptedPin = String(decryptedBytes)

        // Then
        assertNotEquals(wrongPinBytes, decryptedPin)
    }
}
