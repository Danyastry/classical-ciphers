package com.danyastry.encryption.feature.enigma

import com.danyastry.encryption.core.classical.ALPHABET
import com.danyastry.encryption.core.classical.ALPHABET_SIZE

enum class Rotor(internal val wiring: String, val notch: Char) {
    I("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q'),
    II("AJDKSIRUXBLHWTMCQGZNPYFVOE", 'E'),
    III("BDFHJLCPRTXVZNYEIWGAKMUSQO", 'V'),
    IV("ESOVPZJAYQUIRHXLNFTGKDCMWB", 'J'),
    V("VZBRGITYUPSDNHLXAWMJQOFECK", 'Z');

    private val forwardMap = IntArray(ALPHABET_SIZE) { wiring[it] - 'A' }
    private val backwardMap = IntArray(ALPHABET_SIZE).also { map -> forwardMap.forEachIndexed { index, value -> map[value] = index } }

    internal fun forward(signal: Int, offset: Int): Int = (forwardMap[(signal + offset).mod(ALPHABET_SIZE)] - offset).mod(ALPHABET_SIZE)

    internal fun backward(signal: Int, offset: Int): Int = (backwardMap[(signal + offset).mod(ALPHABET_SIZE)] - offset).mod(ALPHABET_SIZE)
}

enum class Reflector(internal val wiring: String) {
    B("YRUHQSLDPXNGOKMIEBFZCWVJAT"),
    C("FVPJIAOYEDRZXWGCTKUQSBNMHL");

    internal fun reflect(signal: Int): Int = wiring[signal] - 'A'
}

enum class PlugboardError { ODD_LETTERS, REPEATED_LETTER }

class Plugboard private constructor(val pairs: List<String>) {

    private val mapping = IntArray(ALPHABET_SIZE) { it }.also { map ->
        pairs.forEach { pair ->
            map[pair[0] - 'A'] = pair[1] - 'A'
            map[pair[1] - 'A'] = pair[0] - 'A'
        }
    }

    internal fun swap(signal: Int): Int = mapping[signal]

    override fun equals(other: Any?): Boolean = other is Plugboard && other.pairs == pairs

    override fun hashCode(): Int = pairs.hashCode()

    override fun toString(): String = "Plugboard(${pairs.joinToString(" ")})"

    companion object {
        val EMPTY = Plugboard(emptyList())

        fun normalize(text: String): String = text.uppercase().filter { it in ALPHABET }

        fun validate(text: String): PlugboardError? {
            val letters = normalize(text)
            return when {
                letters.length % 2 != 0 -> PlugboardError.ODD_LETTERS
                letters.toSet().size != letters.length -> PlugboardError.REPEATED_LETTER
                else -> null
            }
        }

        fun parse(text: String): Plugboard {
            validate(text)?.let { throw IllegalArgumentException("Invalid plugboard '$text': $it") }
            return Plugboard(normalize(text).chunked(2))
        }
    }
}
