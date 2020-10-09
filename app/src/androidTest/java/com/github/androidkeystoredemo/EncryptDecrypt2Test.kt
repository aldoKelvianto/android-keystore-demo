package com.github.androidkeystoredemo

import android.util.Base64
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EncryptDecrypt2Test {

    @Test
    fun encryptDecryptSuccess() {
        // Given
        val pinBytes = "1234".toByteArray()

        // When
        val pair = EncryptDecrypt2.encrypt(pinBytes)
        val cipherBytes = pair.first
        val iv = pair.second
        val plainBytes = EncryptDecrypt2.decrypt(cipherBytes, iv)

        // Then
        assertTrue(plainBytes.contentEquals(pinBytes))
    }

    @Test
    fun encryptDecryptFail() {
        // Given
        val pinBytes = "1234".toByteArray()
        val wrongPinBytes = "12345".toByteArray()

        // When
        val pair = EncryptDecrypt2.encrypt(pinBytes)
        val cipherBytes = pair.first
        val iv = pair.second
        val plainBytes = EncryptDecrypt2.decrypt(cipherBytes, iv)

        // Then
        assertFalse(plainBytes.contentEquals(wrongPinBytes))
    }

    @Test
    fun alwaysCreateDifferentCipherText() {
        // Given
        val list = List(10) {
            "1234".toByteArray()
        }

        // When
        val listOfCipherText = list.map {
            val pair = EncryptDecrypt.encrypt(it)
            Base64.encodeToString(pair.first, Base64.DEFAULT)
        }

        // Then
        assertTrue(listOfCipherText.distinct().size == list.size)
    }
}
