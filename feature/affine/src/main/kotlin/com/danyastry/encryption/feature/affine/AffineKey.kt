package com.danyastry.encryption.feature.affine

import com.danyastry.encryption.core.classical.ALPHABET_SIZE

data class AffineKey(val a: Int, val b: Int) {

    init {
        require(gcd(a.mod(ALPHABET_SIZE), ALPHABET_SIZE) == 1) {
            "a must be coprime with $ALPHABET_SIZE, was $a"
        }
    }

    val inverseOfA: Int
        get() = modularInverse(a.mod(ALPHABET_SIZE))

    companion object {
        val VALID_MULTIPLIERS: List<Int> = (1 until ALPHABET_SIZE).filter { gcd(it, ALPHABET_SIZE) == 1 }

        private fun gcd(x: Int, y: Int): Int = if (y == 0) x else gcd(y, x % y)

        private fun modularInverse(value: Int): Int =
            (1 until ALPHABET_SIZE).first { (value * it) % ALPHABET_SIZE == 1 }
    }
}
