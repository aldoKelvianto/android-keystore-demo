package com.github.androidkeystoredemo

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.Signature
import java.security.cert.Certificate

object VerifyWithKeyPair {

    private const val keystoreAlias = "pin"
    private const val keystoreProvider = "AndroidKeyStore"
    private lateinit var privateKey: PrivateKey
    private lateinit var certificate: Certificate
    private val signatureCipher = Signature.getInstance("SHA256withECDSA")

    init {
        initKeyPair()
    }

    private fun initKeyPair() {
        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC,
            keystoreProvider
        )

        val spec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
            keystoreAlias,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        ).setDigests(KeyProperties.DIGEST_SHA256)
            .build()

        kpg.initialize(spec)
        kpg.generateKeyPair()

        val keyStore = KeyStore.getInstance(keystoreProvider).apply {
            load(null)
        }
        // Get key pair from KeyStore
        val entry = keyStore.getEntry(keystoreAlias, null) as KeyStore.PrivateKeyEntry
        certificate = entry.certificate
        privateKey = entry.privateKey
    }

    fun sign(data: ByteArray): ByteArray = signatureCipher.run {
        initSign(privateKey)
        update(data)
        sign()
    }

    fun verify(signature: ByteArray, data: ByteArray): Boolean = signatureCipher.run {
        initVerify(certificate)
        update(data)
        verify(signature)
    }
}
