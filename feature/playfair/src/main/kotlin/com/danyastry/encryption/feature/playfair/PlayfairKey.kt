package com.danyastry.encryption.feature.playfair

import com.danyastry.encryption.core.classical.ALPHABET

class PlayfairKey(keyword: String) {

    val square: String = (normalize(keyword) + SQUARE_ALPHABET).toCharArray().distinct().joinToString("")

    private val positions: Map<Char, Int> = square.withIndex().associate { (index, letter) -> letter to index }

    fun rowOf(letter: Char): Int = indexOf(letter) / SIZE

    fun columnOf(letter: Char): Int = indexOf(letter) % SIZE

    fun letterAt(row: Int, column: Int): Char = square[row.mod(SIZE) * SIZE + column.mod(SIZE)]

    override fun equals(other: Any?): Boolean = other is PlayfairKey && other.square == square

    override fun hashCode(): Int = square.hashCode()

    override fun toString(): String = "PlayfairKey(square=$square)"

    private fun indexOf(letter: Char): Int =
        requireNotNull(positions[letter]) { "'$letter' is not in the Playfair square" }

    companion object {
        const val SIZE = 5
        val SQUARE_ALPHABET: String = ALPHABET.replace("J", "")

        fun normalize(text: String): String = text.uppercase().filter { it in 'A'..'Z' }.replace('J', 'I')
    }
}
