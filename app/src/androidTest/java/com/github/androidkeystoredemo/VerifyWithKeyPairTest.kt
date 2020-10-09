package com.github.androidkeystoredemo

import android.util.Base64
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VerifyWithKeyPairTest {

    @Test
    fun verifiedSuccess() {
        // Given
        val pin1 = "1234"
        val pin2 = "1234"

        // When
        val signature1 = VerifyWithKeyPair.sign(pin1.toByteArray())
        val signature2 = VerifyWithKeyPair.sign(pin2.toByteArray())

        val isValid1 = VerifyWithKeyPair.verify(signature1, pin2.toByteArray())
        val isValid2 = VerifyWithKeyPair.verify(signature2, pin1.toByteArray())

        // Then
        assertTrue(isValid1)
        assertTrue(isValid2)

        // Don't verify the signature like this, use Signature cipher instead
        assertFalse(signature1.contentEquals(signature2))
    }

    @Test
    fun verifiedFail() {
        // Given
        val pin1 = "1234"
        val pin2 = "12345"

        // When
        val signature1 = VerifyWithKeyPair.sign(pin1.toByteArray())
        val signature2 = VerifyWithKeyPair.sign(pin2.toByteArray())

        val isValid1 = VerifyWithKeyPair.verify(signature1, pin2.toByteArray())
        val isValid2 = VerifyWithKeyPair.verify(signature2, pin1.toByteArray())

        // Then
        assertFalse(isValid1)
        assertFalse(isValid2)
    }

    @Test
    fun alwaysCreateDifferentSignature() {
        // Given
        val list = List(10) {
            "1234".toByteArray()
        }

        // When
        val listOfSignature = list.map {
            val signature = VerifyWithKeyPair.sign(it)
            Base64.encodeToString(signature, Base64.DEFAULT)
        }

        // Then
        assertTrue(listOfSignature.distinct().size == list.size)
    }
}
