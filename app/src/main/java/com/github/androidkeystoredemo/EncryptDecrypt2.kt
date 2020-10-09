package com.github.androidkeystoredemo

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec

object EncryptDecrypt2 {

    private const val keystoreAlias = "pin3"
    private const val keystoreProvider = "AndroidKeyStore"

    // The format is Cipher/Block/Padding
    private val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")

    init {
        initSecretKey()
    }

    private fun initSecretKey() {
        // Step 1: Create specification for the key
        val spec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
            keystoreAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .build()

        // Step 2: Generate the key
        val keyGen =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGen.init(spec)
        // Once you do this, they key is stored in the AndroidKeyStore
        keyGen.generateKey()

    }

    fun encrypt(data: ByteArray): Pair<ByteArray, ByteArray> {
        // Step 3: Get the key from the AndroidKeystore
        val keyStore = KeyStore.getInstance(keystoreProvider).apply {
            load(null)
        }
        val entry = keyStore.getEntry(keystoreAlias, null) as KeyStore.SecretKeyEntry

        cipher.init(Cipher.ENCRYPT_MODE, entry.secretKey)

        val iv = cipher.iv
        val cipherBytes = cipher.doFinal(data)
        return Pair(cipherBytes, iv)
    }

    fun decrypt(data: ByteArray, iv: ByteArray): ByteArray {
        // Step 3: Get the key from the AndroidKeystore
        val keyStore = KeyStore.getInstance(keystoreProvider).apply {
            load(null)
        }
        val entry = keyStore.getEntry(keystoreAlias, null) as KeyStore.SecretKeyEntry
        val spec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, entry.secretKey, spec)

        return cipher.doFinal(data)
    }
}
