package com.github.androidkeystoredemo

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EncryptDecryptTest {

    @Test
    fun encryptDecryptSuccess() {
        // Given
        val pinBytes = "1234".toByteArray()

        // When
        val cipherBytes = EncryptDecrypt.encrypt(pinBytes)
        val plainBytes = EncryptDecrypt.decrypt(cipherBytes)

        // Then
        assertTrue(plainBytes.contentEquals(pinBytes))
    }

    @Test
    fun encryptDecryptFail() {
        // Given
        val pinBytes = "1234".toByteArray()
        val wrongPinBytes = "12345".toByteArray()

        // When
        val cipherBytes = EncryptDecrypt.encrypt(pinBytes)
        val plainBytes = EncryptDecrypt.decrypt(cipherBytes)

        // Then
        assertFalse(plainBytes.contentEquals(wrongPinBytes))
    }
}
