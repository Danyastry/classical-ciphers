package com.danyastry.encryption.feature.hill

import com.danyastry.encryption.core.cryptoapi.TextCipher

class HillCipher : TextCipher<HillKey> {

    override fun encrypt(plaintext: String, key: HillKey): String {
        val letters = normalize(plaintext)
        val padded = letters.padEnd(letters.length + (key.size - letters.length % key.size) % key.size, PADDING)
        return transform(padded, key.matrix)
    }

    override fun decrypt(ciphertext: String, key: HillKey): String {
        val letters = normalize(ciphertext)
        require(letters.length % key.size == 0) {
            "Hill ciphertext length must be a multiple of ${key.size}, was ${letters.length}"
        }
        return transform(letters, key.inverse)
    }

    private fun transform(letters: String, matrix: HillMatrix): String = buildString(letters.length) {
        letters.chunked(matrix.size).forEach { block ->
            val vector = IntArray(block.length) { block[it] - 'A' }
            matrix.multiply(vector).forEach { append('A' + it) }
        }
    }

    companion object {
        const val PADDING = 'X'

        fun normalize(text: String): String = text.uppercase().filter { it in 'A'..'Z' }
    }
}
