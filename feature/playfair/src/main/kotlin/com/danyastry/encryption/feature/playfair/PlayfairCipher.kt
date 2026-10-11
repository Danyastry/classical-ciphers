package com.danyastry.encryption.feature.playfair

import com.danyastry.encryption.core.cryptoapi.TextCipher

enum class PlayfairRule { SAME_ROW, SAME_COLUMN, RECTANGLE }

data class PlayfairStep(val input: String, val output: String, val rule: PlayfairRule)

class PlayfairCipher : TextCipher<PlayfairKey> {

    override fun encrypt(plaintext: String, key: PlayfairKey): String =
        encryptSteps(plaintext, key).joinToString("") { it.output }

    override fun decrypt(ciphertext: String, key: PlayfairKey): String =
        decryptSteps(ciphertext, key).joinToString("") { it.output }

    fun encryptSteps(plaintext: String, key: PlayfairKey): List<PlayfairStep> =
        digraphs(plaintext).map { transform(it, key, direction = 1) }

    fun decryptSteps(ciphertext: String, key: PlayfairKey): List<PlayfairStep> {
        val letters = PlayfairKey.normalize(ciphertext)
        require(letters.length % 2 == 0) { "Playfair ciphertext must have an even number of letters, was ${letters.length}" }
        return letters.chunked(2).map { transform(it, key, direction = -1) }
    }

    fun digraphs(plaintext: String): List<String> {
        val letters = PlayfairKey.normalize(plaintext)
        val pairs = mutableListOf<String>()
        var index = 0
        while (index < letters.length) {
            val first = letters[index]
            val second = letters.getOrNull(index + 1)
            if (second == null || second == first) {
                pairs += "$first${fillerFor(first)}"
                index += 1
            } else {
                pairs += "$first$second"
                index += 2
            }
        }
        return pairs
    }

    private fun fillerFor(letter: Char): Char = if (letter == 'X') 'Q' else 'X'

    private fun transform(pair: String, key: PlayfairKey, direction: Int): PlayfairStep {
        val (a, b) = pair[0] to pair[1]
        val rowA = key.rowOf(a)
        val columnA = key.columnOf(a)
        val rowB = key.rowOf(b)
        val columnB = key.columnOf(b)
        return when {
            rowA == rowB -> PlayfairStep(
                input = pair,
                output = "${key.letterAt(rowA, columnA + direction)}${key.letterAt(rowB, columnB + direction)}",
                rule = PlayfairRule.SAME_ROW,
            )
            columnA == columnB -> PlayfairStep(
                input = pair,
                output = "${key.letterAt(rowA + direction, columnA)}${key.letterAt(rowB + direction, columnB)}",
                rule = PlayfairRule.SAME_COLUMN,
            )
            else -> PlayfairStep(
                input = pair,
                output = "${key.letterAt(rowA, columnB)}${key.letterAt(rowB, columnA)}",
                rule = PlayfairRule.RECTANGLE,
            )
        }
    }
}
