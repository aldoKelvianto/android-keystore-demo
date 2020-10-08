package com.github.androidkeystoredemo

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object EncryptDecrypt {

    private const val keystoreAlias = "pin2"
    private const val keystoreProvider = "AndroidKeyStore"
    private lateinit var secretKey: SecretKey
    private val cipher = Cipher.getInstance("AES/GCM/NoPadding")

    init {
        initSecretKey()
    }

    private fun initSecretKey() {
        val keyGen =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

        val spec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
            keystoreAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGen.init(spec)
        keyGen.generateKey()

        val keyStore = KeyStore.getInstance(keystoreProvider).apply {
            load(null)
        }
        // Get private key from KeyStore
        val entry = keyStore.getEntry(keystoreAlias, null) as KeyStore.SecretKeyEntry
        secretKey = entry.secretKey
    }

    fun encrypt(data: ByteArray): Pair<ByteArray, ByteArray> {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val iv = cipher.iv
        val cipherBytes = cipher.doFinal(data)
        return Pair(cipherBytes, iv)
    }

    fun decrypt(data: ByteArray, iv: ByteArray): ByteArray {
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        return cipher.doFinal(data)
    }
}
