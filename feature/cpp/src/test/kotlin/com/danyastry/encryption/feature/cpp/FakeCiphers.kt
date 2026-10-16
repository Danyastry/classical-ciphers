package com.danyastry.encryption.feature.cpp

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.cryptoapi.ByteCipher
import com.danyastry.encryption.core.cryptoapi.TextCipher

object FakeCiphers {

    fun reference(): CipherSet = CipherSet(
        caesar = textCipher { text, shift -> text.map { if (it in 'a'..'z') 'a' + (it - 'a' + shift).mod(26) else it }.joinToString("") },
        vigenere = textCipher<Keyword> { text, key -> text.reversed() + key.letters },
        xor = byteCipher { data, key -> ByteArray(data.size) { (data[it].toInt() xor key[it % key.size].toInt()).toByte() } },
    )

    fun broken(): CipherSet = CipherSet(
        caesar = textCipher { text, _ -> text.uppercase() },
        vigenere = textCipher<Keyword> { text, _ -> text },
        xor = byteCipher { data, _ -> data.copyOf() },
    )

    private fun <K> textCipher(transform: (String, K) -> String) = object : TextCipher<K> {
        override fun encrypt(plaintext: String, key: K): String = transform(plaintext, key)
        override fun decrypt(ciphertext: String, key: K): String = transform(ciphertext, key)
    }

    private fun byteCipher(transform: (ByteArray, ByteArray) -> ByteArray) = object : ByteCipher<ByteArray> {
        override fun encrypt(plaintext: ByteArray, key: ByteArray): ByteArray = transform(plaintext, key)
        override fun decrypt(ciphertext: ByteArray, key: ByteArray): ByteArray = transform(ciphertext, key)
    }
}
