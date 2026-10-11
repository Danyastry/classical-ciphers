package com.danyastry.encryption.core.classical

class PolybiusSquare private constructor(val alphabet: String, val letters: String) {

    val size: Int = sizeOf(alphabet)

    private val positions: Map<Char, Int> = letters.withIndex().associate { (index, letter) -> letter to index }

    val rows: List<String>
        get() = letters.chunked(size)

    operator fun contains(char: Char): Boolean = char in positions

    fun rowOf(char: Char): Int = indexOf(char) / size

    fun columnOf(char: Char): Int = indexOf(char) % size

    fun letterAt(row: Int, column: Int): Char = letters[row.mod(size) * size + column.mod(size)]

    fun normalize(text: String): String = normalize(text, alphabet)

    override fun equals(other: Any?): Boolean =
        other is PolybiusSquare && other.alphabet == alphabet && other.letters == letters

    override fun hashCode(): Int = 31 * alphabet.hashCode() + letters.hashCode()

    override fun toString(): String = "PolybiusSquare(${rows.joinToString("/")})"

    private fun indexOf(char: Char): Int = requireNotNull(positions[char]) { "'$char' is not in the square" }

    companion object {
        const val LATIN_25 = "ABCDEFGHIKLMNOPQRSTUVWXYZ"
        const val LATIN_25_NO_Q = "ABCDEFGHIJKLMNOPRSTUVWXYZ"
        const val ALPHANUMERIC_36 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

        fun fromKeyword(keyword: String = "", alphabet: String = LATIN_25): PolybiusSquare {
            sizeOf(alphabet)
            require(alphabet.toSet().size == alphabet.length) { "Square alphabet must not repeat characters" }
            val letters = (normalize(keyword, alphabet) + alphabet).toCharArray().distinct().joinToString("")
            return PolybiusSquare(alphabet, letters)
        }

        fun normalize(text: String, alphabet: String): String {
            val upper = text.uppercase().let { if ('J' !in alphabet) it.replace('J', 'I') else it }
            return upper.filter { it in alphabet }
        }

        private fun sizeOf(alphabet: String): Int {
            val size = generateSequence(1) { it + 1 }.first { it * it >= alphabet.length }
            require(size * size == alphabet.length) { "Square alphabet length must be a perfect square, was ${alphabet.length}" }
            return size
        }
    }
}
