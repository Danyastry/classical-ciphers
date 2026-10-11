package com.danyastry.encryption.feature.hill

import com.danyastry.encryption.core.classical.ALPHABET
import kotlin.random.Random

enum class HillKeyError { WRONG_LENGTH, NOT_INVERTIBLE }

class HillKey(val matrix: HillMatrix) {

    val inverse: HillMatrix = requireNotNull(matrix.inverseOrNull()) {
        "Key matrix is not invertible mod 26, determinant is ${matrix.determinant}"
    }

    val size: Int
        get() = matrix.size

    override fun equals(other: Any?): Boolean = other is HillKey && other.matrix == matrix

    override fun hashCode(): Int = matrix.hashCode()

    override fun toString(): String = "HillKey(${matrix.rows})"

    companion object {
        fun validate(letters: String, size: Int): HillKeyError? = when {
            letters.length != size * size || letters.any { it !in 'A'..'Z' } -> HillKeyError.WRONG_LENGTH
            !HillMatrix.fromLetters(letters, size).isInvertible -> HillKeyError.NOT_INVERTIBLE
            else -> null
        }

        fun fromLetters(letters: String, size: Int): HillKey = HillKey(HillMatrix.fromLetters(letters, size))

        fun randomLetters(size: Int, random: Random): String =
            generateSequence { String(CharArray(size * size) { ALPHABET[random.nextInt(ALPHABET.length)] }) }
                .first { validate(it, size) == null }
    }
}
