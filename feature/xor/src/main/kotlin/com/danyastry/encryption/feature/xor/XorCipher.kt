package com.danyastry.encryption.feature.xor

import com.danyastry.encryption.core.cryptoapi.ByteCipher
import kotlin.experimental.xor

class XorCipher : ByteCipher<ByteArray> {

    override fun encrypt(plaintext: ByteArray, key: ByteArray): ByteArray = xorWith(plaintext, key)

    override fun decrypt(ciphertext: ByteArray, key: ByteArray): ByteArray = xorWith(ciphertext, key)

    fun recoverKeystream(ciphertext: ByteArray, knownPlaintext: ByteArray): ByteArray =
        ByteArray(minOf(ciphertext.size, knownPlaintext.size)) { ciphertext[it] xor knownPlaintext[it] }

    private fun xorWith(data: ByteArray, key: ByteArray): ByteArray {
        require(key.isNotEmpty()) { "XOR key must not be empty" }
        return ByteArray(data.size) { data[it] xor key[it % key.size] }
    }
}
