package com.danyastry.encryption.feature.railfence

import com.danyastry.encryption.core.cryptoapi.TextCipher

class RailFenceCipher : TextCipher<Int> {

    override fun encrypt(plaintext: String, key: Int): String {
        val order = readingOrder(plaintext.length, key)
        return buildString(plaintext.length) { order.forEach { append(plaintext[it]) } }
    }

    override fun decrypt(ciphertext: String, key: Int): String {
        val order = readingOrder(ciphertext.length, key)
        val plaintext = CharArray(ciphertext.length)
        order.forEachIndexed { cipherIndex, plainIndex -> plaintext[plainIndex] = ciphertext[cipherIndex] }
        return String(plaintext)
    }

    fun railPattern(length: Int, rails: Int): List<Int> {
        require(rails >= 1) { "Rail count must be at least 1, was $rails" }
        if (rails == 1) return List(length) { 0 }
        val cycle = 2 * (rails - 1)
        return List(length) { index ->
            val step = index % cycle
            if (step < rails) step else cycle - step
        }
    }

    private fun readingOrder(length: Int, rails: Int): List<Int> {
        val pattern = railPattern(length, rails)
        return (0 until rails).flatMap { rail -> pattern.indices.filter { pattern[it] == rail } }
    }
}
