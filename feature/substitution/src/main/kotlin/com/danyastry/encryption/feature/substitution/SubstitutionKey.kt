package com.danyastry.encryption.feature.substitution

import com.danyastry.encryption.core.classical.ALPHABET
import com.danyastry.encryption.core.classical.ALPHABET_SIZE
import kotlin.random.Random

enum class KeyError { WRONG_LENGTH, INVALID_CHARACTERS, DUPLICATE_LETTERS }

data class SubstitutionKey(val alphabet: String) {

    init {
        validate(alphabet)?.let { error -> throw IllegalArgumentException("Invalid substitution key: $error") }
    }

    internal val inverse: IntArray = IntArray(ALPHABET_SIZE).also { inverse ->
        alphabet.forEachIndexed { index, letter -> inverse[letter - 'A'] = index }
    }

    companion object {
        fun validate(alphabet: String): KeyError? = when {
            alphabet.any { it !in 'A'..'Z' } -> KeyError.INVALID_CHARACTERS
            alphabet.toSet().size != alphabet.length -> KeyError.DUPLICATE_LETTERS
            alphabet.length != ALPHABET_SIZE -> KeyError.WRONG_LENGTH
            else -> null
        }

        fun fromKeyword(keyword: String): SubstitutionKey {
            val letters = keyword.uppercase().filter { it in 'A'..'Z' } + ALPHABET
            return SubstitutionKey(letters.toCharArray().distinct().joinToString(""))
        }

        fun random(random: Random): SubstitutionKey =
            SubstitutionKey(ALPHABET.toList().shuffled(random).joinToString(""))
    }
}
