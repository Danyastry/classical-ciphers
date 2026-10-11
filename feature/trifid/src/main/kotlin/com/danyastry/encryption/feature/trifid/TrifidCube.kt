package com.danyastry.encryption.feature.trifid

import com.danyastry.encryption.core.classical.ALPHABET

class TrifidCube private constructor(val letters: String) {

    private val positions: Map<Char, Int> = letters.withIndex().associate { (index, letter) -> letter to index }

    val layers: List<List<String>>
        get() = letters.chunked(LAYER_CELLS).map { it.chunked(SIZE) }

    fun coordinatesOf(char: Char): Triple<Int, Int, Int> {
        val index = requireNotNull(positions[char]) { "'$char' is not in the Trifid cube" }
        return Triple(index / LAYER_CELLS, index % LAYER_CELLS / SIZE, index % SIZE)
    }

    fun letterAt(layer: Int, row: Int, column: Int): Char = letters[layer * LAYER_CELLS + row * SIZE + column]

    override fun equals(other: Any?): Boolean = other is TrifidCube && other.letters == letters

    override fun hashCode(): Int = letters.hashCode()

    override fun toString(): String = "TrifidCube($letters)"

    companion object {
        const val SIZE = 3
        private const val LAYER_CELLS = SIZE * SIZE
        const val CUBE_ALPHABET = "$ALPHABET+"

        fun fromKeyword(keyword: String = ""): TrifidCube =
            TrifidCube((normalize(keyword) + CUBE_ALPHABET).toCharArray().distinct().joinToString(""))

        fun normalize(text: String): String = text.uppercase().filter { it in CUBE_ALPHABET }
    }
}
