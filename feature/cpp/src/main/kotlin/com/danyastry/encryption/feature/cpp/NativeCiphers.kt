package com.danyastry.encryption.feature.cpp

import com.danyastry.encryption.core.classical.ALPHABET_SIZE
import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.cryptoapi.ByteCipher
import com.danyastry.encryption.core.cryptoapi.TextCipher

internal object NativeCiphers {

    init {
        System.loadLibrary("encryption_cpp")
    }

    external fun caesar(text: String, shift: Int): String

    external fun vigenere(text: String, shifts: IntArray, direction: Int): String

    external fun xor(data: ByteArray, key: ByteArray): ByteArray
}

class NativeCaesarCipher : TextCipher<Int> {

    override fun encrypt(plaintext: String, key: Int): String = NativeCiphers.caesar(plaintext, key.mod(ALPHABET_SIZE))

    override fun decrypt(ciphertext: String, key: Int): String =
        NativeCiphers.caesar(ciphertext, ALPHABET_SIZE - key.mod(ALPHABET_SIZE))
}

class NativeVigenereCipher : TextCipher<Keyword> {

    override fun encrypt(plaintext: String, key: Keyword): String = NativeCiphers.vigenere(plaintext, key.shifts(), FORWARD)

    override fun decrypt(ciphertext: String, key: Keyword): String = NativeCiphers.vigenere(ciphertext, key.shifts(), BACKWARD)

    private fun Keyword.shifts(): IntArray = IntArray(length) { shiftAt(it) }

    private companion object {
        const val FORWARD = 1
        const val BACKWARD = -1
    }
}

class NativeXorCipher : ByteCipher<ByteArray> {

    override fun encrypt(plaintext: ByteArray, key: ByteArray): ByteArray = xorWith(plaintext, key)

    override fun decrypt(ciphertext: ByteArray, key: ByteArray): ByteArray = xorWith(ciphertext, key)

    private fun xorWith(data: ByteArray, key: ByteArray): ByteArray {
        require(key.isNotEmpty()) { "XOR key must not be empty" }
        return NativeCiphers.xor(data, key)
    }
}
