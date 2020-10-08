package com.github.androidkeystoredemo

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.androidkeystoredemo.EncryptDecrypt
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EncryptDecryptTest {

    @Test
    fun encryptDecryptSuccess() {
        // Given
        val pinBytes = "1234".toByteArray()

        // When
        val pair = EncryptDecrypt.encrypt(pinBytes)
        val cipherBytes = pair.first
        val iv = pair.second
        val plainBytes = EncryptDecrypt.decrypt(cipherBytes, iv)

        // Then
        assertTrue(plainBytes.contentEquals(pinBytes))
    }

    @Test
    fun encryptDecryptFail() {
        // Given
        val pinBytes = "1234".toByteArray()
        val wrongPinBytes = "12345".toByteArray()

        // When
        val pair = EncryptDecrypt.encrypt(pinBytes)
        val cipherBytes = pair.first
        val iv = pair.second
        val plainBytes = EncryptDecrypt.decrypt(cipherBytes, iv)

        // Then
        assertFalse(plainBytes.contentEquals(wrongPinBytes))
    }
}
