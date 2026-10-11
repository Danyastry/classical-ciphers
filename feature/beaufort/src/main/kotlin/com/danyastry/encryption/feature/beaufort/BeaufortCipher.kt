package com.danyastry.encryption.feature.beaufort

import com.danyastry.encryption.core.cryptoapi.TextCipher

class BeaufortCipher : TextCipher<BeaufortKey> {

    override fun encrypt(plaintext: String, key: BeaufortKey): String = transform(plaintext, key)

    override fun decrypt(ciphertext: String, key: BeaufortKey): String = transform(ciphertext, key)

    private fun transform(text: String, key: BeaufortKey): String {
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
                    val shift = key.shifts[keyIndex % key.shifts.size]
                    append(base + (shift - (char - base) + ALPHABET_SIZE) % ALPHABET_SIZE)
                    keyIndex++
                }
            }
        }
    }

    private companion object {
        const val ALPHABET_SIZE = 26
    }
}
