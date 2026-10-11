package com.danyastry.encryption.feature.affine

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class AffineKeyTest {

    @Test
    fun `valid multipliers are the twelve values coprime with 26`() {
        assertThat(AffineKey.VALID_MULTIPLIERS)
            .containsExactly(1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25)
            .inOrder()
    }

    @Test
    fun `rejects multipliers sharing a factor with 26`() {
        listOf(0, 2, 4, 13, 26, -2, 39).forEach { a ->
            assertThrows(IllegalArgumentException::class.java) { AffineKey(a = a, b = 0) }
        }
    }

    @Test
    fun `accepts multipliers coprime with 26 after normalization`() {
        listOf(27, -1, -25, 51).forEach { a -> AffineKey(a = a, b = 0) }
    }

    @Test
    fun `computes the modular inverse of a`() {
        assertThat(AffineKey(a = 5, b = 0).inverseOfA).isEqualTo(21)
        assertThat(AffineKey(a = 7, b = 0).inverseOfA).isEqualTo(15)
        assertThat(AffineKey(a = 1, b = 0).inverseOfA).isEqualTo(1)
        assertThat(AffineKey(a = 25, b = 0).inverseOfA).isEqualTo(25)
    }

    @Test
    fun `inverse times a is one for every valid multiplier`() {
        AffineKey.VALID_MULTIPLIERS.forEach { a ->
            assertThat((a * AffineKey(a = a, b = 0).inverseOfA) % 26).isEqualTo(1)
        }
    }
}
