package com.danyastry.encryption.feature.hill

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class HillMatrixTest {

    @Test
    fun `builds a matrix from key letters row by row`() {
        assertThat(HillMatrix.fromLetters("HILL", 2).rows).isEqualTo(listOf(listOf(7, 8), listOf(11, 11)))
        assertThat(HillMatrix.fromLetters("GYBNQKURP", 3).rows)
            .isEqualTo(listOf(listOf(6, 24, 1), listOf(13, 16, 10), listOf(20, 17, 15)))
    }

    @Test
    fun `computes the determinant mod 26`() {
        assertThat(HillMatrix.fromLetters("HILL", 2).determinant).isEqualTo(15)
        assertThat(HillMatrix.fromLetters("GYBNQKURP", 3).determinant).isEqualTo(25)
        assertThat(HillMatrix.fromLetters("ABCD", 2).determinant).isEqualTo(24)
        assertThat(HillMatrix.fromLetters("BBBB", 2).determinant).isEqualTo(0)
    }

    @Test
    fun `matches the reference inverse matrices`() {
        assertThat(HillMatrix.fromLetters("HILL", 2).inverseOrNull()?.rows)
            .isEqualTo(listOf(listOf(25, 22), listOf(1, 23)))
        assertThat(HillMatrix.fromLetters("GYBNQKURP", 3).inverseOrNull()?.rows)
            .isEqualTo(listOf(listOf(8, 5, 10), listOf(21, 8, 21), listOf(21, 12, 8)))
    }

    @Test
    fun `matrix times its inverse is the identity`() {
        listOf("HILL" to 2, "DDCF" to 2, "GYBNQKURP" to 3).forEach { (letters, size) ->
            val matrix = HillMatrix.fromLetters(letters, size)
            val inverse = requireNotNull(matrix.inverseOrNull())

            (0 until size).forEach { column ->
                val unit = IntArray(size) { if (it == column) 1 else 0 }
                assertThat(matrix.multiply(inverse.multiply(unit)).toList()).isEqualTo(unit.toList())
            }
        }
    }

    @Test
    fun `matrices with a determinant sharing a factor with 26 have no inverse`() {
        listOf("ABCD", "BBBB", "NAAB").forEach { letters ->
            val matrix = HillMatrix.fromLetters(letters, 2)

            assertThat(matrix.isInvertible).isFalse()
            assertThat(matrix.inverseOrNull()).isNull()
        }
    }

    @Test
    fun `normalizes entries mod 26`() {
        assertThat(HillMatrix(2, listOf(33, -18, 37, 11)).rows).isEqualTo(listOf(listOf(7, 8), listOf(11, 11)))
    }

    @Test
    fun `rejects unsupported sizes and wrong entry counts`() {
        assertThrows(IllegalArgumentException::class.java) { HillMatrix(1, listOf(1)) }
        assertThrows(IllegalArgumentException::class.java) { HillMatrix(4, List(16) { 1 }) }
        assertThrows(IllegalArgumentException::class.java) { HillMatrix(2, listOf(1, 2, 3)) }
        assertThrows(IllegalArgumentException::class.java) { HillMatrix.fromLetters("HIL", 2) }
        assertThrows(IllegalArgumentException::class.java) { HillMatrix.fromLetters("hill", 2) }
    }
}
