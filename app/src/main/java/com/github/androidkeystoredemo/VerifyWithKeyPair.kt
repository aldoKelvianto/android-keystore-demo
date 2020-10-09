package com.github.androidkeystoredemo

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.Signature

object VerifyWithKeyPair {

    private const val keystoreAlias = "pin"
    private const val keystoreProvider = "AndroidKeyStore"

    private val signatureCipher = Signature.getInstance("SHA256withECDSA")

    init {
        // Step 1: Create specification for the key
        val spec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
            keystoreAlias,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        ).setDigests(KeyProperties.DIGEST_SHA256)
            .build()

        // Step 2: Generate the keys
        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC,
            keystoreProvider
        )
        kpg.initialize(spec)
        // Once you do this, they keys are stored in the AndroidKeyStore
        kpg.generateKeyPair()
    }

    fun sign(data: ByteArray): ByteArray {
        // Step 3: Get key pair from KeyStore
        val keyStore = KeyStore.getInstance(keystoreProvider).apply {
            load(null)
        }
        val entry = keyStore.getEntry(keystoreAlias, null)
        val privateKey = (entry as KeyStore.PrivateKeyEntry).privateKey

        // Step 4: Sign
        return signatureCipher.run {
            initSign(privateKey)
            update(data)
            sign()
        }
    }

    fun verify(signature: ByteArray, data: ByteArray): Boolean {
        // Step 3: Get key pair from KeyStore
        val keyStore = KeyStore.getInstance(keystoreProvider).apply {
            load(null)
        }
        val entry = keyStore.getEntry(keystoreAlias, null)
        val certificate = (entry as KeyStore.PrivateKeyEntry).certificate

        // Step 4: Verify
        return signatureCipher.run {
            initVerify(certificate)
            update(data)
            verify(signature)
        }
    }
}
