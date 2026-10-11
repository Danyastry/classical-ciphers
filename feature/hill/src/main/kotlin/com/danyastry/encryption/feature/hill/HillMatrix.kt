package com.danyastry.encryption.feature.hill

import com.danyastry.encryption.core.classical.ALPHABET_SIZE

class HillMatrix(val size: Int, entries: List<Int>) {

    init {
        require(size in SUPPORTED_SIZES) { "Matrix size must be one of $SUPPORTED_SIZES, was $size" }
        require(entries.size == size * size) { "A ${size}x$size matrix needs ${size * size} entries, got ${entries.size}" }
    }

    private val values: IntArray = IntArray(size * size) { entries[it].mod(ALPHABET_SIZE) }

    val rows: List<List<Int>>
        get() = (0 until size).map { row -> (0 until size).map { column -> this[row, column] } }

    val determinant: Int = determinantOf(rows).mod(ALPHABET_SIZE)

    val isInvertible: Boolean
        get() = modularInverse(determinant) != null

    operator fun get(row: Int, column: Int): Int = values[row * size + column]

    fun inverseOrNull(): HillMatrix? {
        val determinantInverse = modularInverse(determinant) ?: return null
        val matrix = rows
        val adjugate = List(size * size) { index ->
            val row = index / size
            val column = index % size
            cofactor(matrix, row = column, column = row)
        }
        return HillMatrix(size, adjugate.map { it * determinantInverse })
    }

    fun multiply(vector: IntArray): IntArray {
        require(vector.size == size) { "Vector must have $size entries, got ${vector.size}" }
        return IntArray(size) { row ->
            (0 until size).sumOf { column -> this[row, column] * vector[column] }.mod(ALPHABET_SIZE)
        }
    }

    override fun equals(other: Any?): Boolean =
        other is HillMatrix && other.size == size && other.values.contentEquals(values)

    override fun hashCode(): Int = 31 * size + values.contentHashCode()

    override fun toString(): String = "HillMatrix($rows)"

    companion object {
        val SUPPORTED_SIZES = 2..3

        fun fromLetters(letters: String, size: Int): HillMatrix {
            require(letters.length == size * size) { "A ${size}x$size key needs ${size * size} letters, got ${letters.length}" }
            require(letters.all { it in 'A'..'Z' }) { "Key letters must be A-Z, was '$letters'" }
            return HillMatrix(size, letters.map { it - 'A' })
        }

        private fun determinantOf(matrix: List<List<Int>>): Int {
            if (matrix.size == 1) return matrix[0][0]
            return matrix.indices.sumOf { column -> matrix[0][column] * cofactor(matrix, row = 0, column = column) }
        }

        private fun cofactor(matrix: List<List<Int>>, row: Int, column: Int): Int {
            val minor = matrix.filterIndexed { index, _ -> index != row }
                .map { values -> values.filterIndexed { index, _ -> index != column } }
            val sign = if ((row + column) % 2 == 0) 1 else -1
            return sign * determinantOf(minor)
        }

        private fun modularInverse(value: Int): Int? =
            (1 until ALPHABET_SIZE).firstOrNull { (value * it) % ALPHABET_SIZE == 1 }
    }
}
