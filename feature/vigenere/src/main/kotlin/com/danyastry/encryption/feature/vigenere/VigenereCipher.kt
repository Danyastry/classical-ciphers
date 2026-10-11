package com.danyastry.encryption.feature.vigenere

import com.danyastry.encryption.core.cryptoapi.TextCipher

class VigenereCipher : TextCipher<VigenereKey> {

    override fun encrypt(plaintext: String, key: VigenereKey): String = transform(plaintext, key, direction = 1)

    override fun decrypt(ciphertext: String, key: VigenereKey): String = transform(ciphertext, key, direction = -1)

    private fun transform(text: String, key: VigenereKey, direction: Int): String {
        var keyIndex = 0
        return buildString(text.length) {
            text.forEach { char ->
                val base = when (char) {
                    in 'a'..'z' -> 'a'
                    in 'A'..'Z' -> 'A'
                    else -> null
                }
                if (base == null) {
                    append(char)
                } else {
                    val shift = key.shifts[keyIndex % key.shifts.size] * direction
                    append(base + (char - base + shift + ALPHABET_SIZE) % ALPHABET_SIZE)
                    keyIndex++
                }
            }
        }
    }

    private companion object {
        const val ALPHABET_SIZE = 26
    }
}
