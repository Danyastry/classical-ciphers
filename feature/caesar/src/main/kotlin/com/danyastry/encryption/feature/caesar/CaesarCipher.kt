package com.danyastry.encryption.feature.caesar

import com.danyastry.encryption.core.classical.ALPHABET_SIZE
import com.danyastry.encryption.core.classical.mapLetters
import com.danyastry.encryption.core.cryptoapi.TextCipher

data class BruteForceCandidate(val shift: Int, val plaintext: String)

class CaesarCipher : TextCipher<Int> {

    override fun encrypt(plaintext: String, key: Int): String {
        val offset = key.mod(ALPHABET_SIZE)
        return plaintext.mapLetters { it + offset }
    }

    override fun decrypt(ciphertext: String, key: Int): String {
        val offset = key.mod(ALPHABET_SIZE)
        return ciphertext.mapLetters { it - offset }
    }

    fun bruteForce(ciphertext: String): List<BruteForceCandidate> =
        (0 until ALPHABET_SIZE).map { shift -> BruteForceCandidate(shift, decrypt(ciphertext, shift)) }
}
