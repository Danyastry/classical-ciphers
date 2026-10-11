package com.danyastry.encryption.feature.onetimepad

import com.danyastry.encryption.core.classical.ALPHABET
import com.danyastry.encryption.core.classical.ALPHABET_SIZE
import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.classical.asciiLetters
import com.danyastry.encryption.core.classical.mapLetters
import com.danyastry.encryption.core.cryptoapi.TextCipher
import kotlin.random.Random

class OneTimePadCipher : TextCipher<Keyword> {

    override fun encrypt(plaintext: String, key: Keyword): String {
        requireLongEnough(plaintext, key)
        var position = 0
        return plaintext.mapLetters { it + key.shiftAt(position++) }
    }

    override fun decrypt(ciphertext: String, key: Keyword): String {
        requireLongEnough(ciphertext, key)
        var position = 0
        return ciphertext.mapLetters { it - key.shiftAt(position++) }
    }

    fun keyFor(ciphertext: String, plaintext: String): String {
        val cipherLetters = ciphertext.asciiLetters().uppercase()
        val plainLetters = plaintext.asciiLetters().uppercase()
        require(cipherLetters.length == plainLetters.length) {
            "Both texts need the same number of letters, got ${cipherLetters.length} and ${plainLetters.length}"
        }
        return cipherLetters.indices.map { ALPHABET[(cipherLetters[it] - plainLetters[it]).mod(ALPHABET_SIZE)] }.joinToString("")
    }

    private fun requireLongEnough(text: String, key: Keyword) {
        val letters = lettersIn(text)
        require(key.length >= letters) { "A one-time pad must be at least as long as the message: need $letters letters, got ${key.length}" }
    }

    companion object {
        fun lettersIn(text: String): Int = text.asciiLetters().length

        fun randomKey(length: Int, random: Random): String =
            String(CharArray(length) { ALPHABET[random.nextInt(ALPHABET_SIZE)] })
    }
}
