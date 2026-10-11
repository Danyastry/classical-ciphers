package com.danyastry.encryption.feature.bacon

import com.danyastry.encryption.core.classical.ALPHABET
import com.danyastry.encryption.core.classical.isAsciiLetter

enum class BaconAlphabet(internal val letters: String) {
    CLASSIC_24("ABCDEFGHIKLMNOPQRSTUWXYZ"),
    MODERN_26(ALPHABET),
}

class BaconCipher(private val alphabet: BaconAlphabet = BaconAlphabet.CLASSIC_24) {

    fun encode(plaintext: String): String =
        plaintext.uppercase().filter { it in 'A'..'Z' }.map { codeFor(it) }.joinToString(" ")

    fun decode(code: String): String {
        val symbols = code.uppercase().filter { it == 'A' || it == 'B' }
        require(symbols.length % GROUP_SIZE == 0) {
            "Bacon code must be groups of $GROUP_SIZE A/B symbols, got ${symbols.length} symbols"
        }
        return symbols.chunked(GROUP_SIZE).map { letterFor(it) }.joinToString("")
    }

    fun requiredCoverLetters(message: String): Int = encode(message).count { it != ' ' }

    fun hide(message: String, cover: String): String {
        val symbols = encode(message).filter { it != ' ' }
        val coverLetters = cover.count { it.isAsciiLetter() }
        require(coverLetters >= symbols.length) {
            "Cover text needs at least ${symbols.length} letters, has $coverLetters"
        }
        var index = 0
        return buildString {
            for (char in cover) {
                if (index == symbols.length) break
                if (char.isAsciiLetter()) {
                    append(if (symbols[index] == 'B') char.uppercaseChar() else char.lowercaseChar())
                    index++
                } else {
                    append(char)
                }
            }
        }
    }

    fun reveal(stegotext: String): String {
        val symbols = stegotext.filter { it.isAsciiLetter() }.map { if (it.isUpperCase()) 'B' else 'A' }
        val complete = symbols.size - symbols.size % GROUP_SIZE
        return decode(symbols.take(complete).joinToString(""))
    }

    private fun codeFor(letter: Char): String {
        val normalized = if (alphabet == BaconAlphabet.CLASSIC_24) CLASSIC_MERGES[letter] ?: letter else letter
        val index = alphabet.letters.indexOf(normalized)
        return (GROUP_SIZE - 1 downTo 0).map { bit -> if ((index shr bit) and 1 == 1) 'B' else 'A' }.joinToString("")
    }

    private fun letterFor(group: String): Char {
        val index = group.fold(0) { value, symbol -> value * 2 + if (symbol == 'B') 1 else 0 }
        return alphabet.letters.getOrElse(index) { UNKNOWN }
    }

    companion object {
        const val GROUP_SIZE = 5
        const val UNKNOWN = '?'
        private val CLASSIC_MERGES = mapOf('J' to 'I', 'V' to 'U')
    }
}
