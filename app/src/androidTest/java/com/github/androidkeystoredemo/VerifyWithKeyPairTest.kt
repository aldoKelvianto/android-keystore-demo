package com.github.androidkeystoredemo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
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
        // Don't verify the signature like this, use Signature cipher instead
        assertNotEquals(signature1, signature2)
        assertTrue(isValid1)
        assertTrue(isValid2)
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

}
