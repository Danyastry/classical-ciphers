package com.danyastry.encryption.feature.scytale

import com.danyastry.encryption.core.cryptoapi.TextCipher

class ScytaleCipher : TextCipher<Int> {

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

    fun wrap(text: String, turns: Int): List<String> {
        val length = lettersAlongRod(text.length, turns)
        return if (length == 0) emptyList() else text.chunked(length)
    }

    fun lettersAlongRod(length: Int, turns: Int): Int {
        require(turns >= 1) { "The rod needs at least 1 letter around it, was $turns" }
        return (length + turns - 1) / turns
    }

    private fun readingOrder(length: Int, turns: Int): List<Int> {
        val columns = lettersAlongRod(length, turns)
        return (0 until columns).flatMap { column ->
            (0 until turns).map { row -> row * columns + column }.filter { it < length }
        }
    }
}
