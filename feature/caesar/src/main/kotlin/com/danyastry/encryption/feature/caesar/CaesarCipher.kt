package com.danyastry.encryption.feature.caesar

import com.danyastry.encryption.core.cryptoapi.TextCipher

data class BruteForceCandidate(val shift: Int, val plaintext: String)

class CaesarCipher : TextCipher<Int> {

    override fun encrypt(plaintext: String, key: Int): String =
        shift(plaintext, key.mod(ALPHABET_SIZE))

    override fun decrypt(ciphertext: String, key: Int): String =
        shift(ciphertext, (ALPHABET_SIZE - key.mod(ALPHABET_SIZE)) % ALPHABET_SIZE)

    fun bruteForce(ciphertext: String): List<BruteForceCandidate> =
        (0 until ALPHABET_SIZE).map { shift -> BruteForceCandidate(shift, decrypt(ciphertext, shift)) }

    private fun shift(text: String, offset: Int): String {
        if (offset == 0) return text
        return buildString(text.length) {
            text.forEach { append(shiftLetter(it, offset)) }
        }
    }

    private fun shiftLetter(char: Char, offset: Int): Char = when (char) {
        in 'a'..'z' -> 'a' + (char - 'a' + offset) % ALPHABET_SIZE
        in 'A'..'Z' -> 'A' + (char - 'A' + offset) % ALPHABET_SIZE
        else -> char
    }

    companion object {
        const val ALPHABET_SIZE = 26
    }
}
