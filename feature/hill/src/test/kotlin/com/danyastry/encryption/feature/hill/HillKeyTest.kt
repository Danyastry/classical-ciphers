package com.danyastry.encryption.feature.hill

import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Assert.assertThrows
import org.junit.Test

class HillKeyTest {

    @Test
    fun `validates key letters`() {
        assertThat(HillKey.validate("HILL", 2)).isNull()
        assertThat(HillKey.validate("GYBNQKURP", 3)).isNull()
        assertThat(HillKey.validate("HIL", 2)).isEqualTo(HillKeyError.WRONG_LENGTH)
        assertThat(HillKey.validate("HILL", 3)).isEqualTo(HillKeyError.WRONG_LENGTH)
        assertThat(HillKey.validate("hill", 2)).isEqualTo(HillKeyError.WRONG_LENGTH)
        assertThat(HillKey.validate("ABCD", 2)).isEqualTo(HillKeyError.NOT_INVERTIBLE)
    }

    @Test
    fun `rejects keys that cannot be inverted`() {
        assertThrows(IllegalArgumentException::class.java) { HillKey.fromLetters("ABCD", 2) }
    }

    @Test
    fun `random keys are always invertible`() {
        val random = Random(seed = 11)

        listOf(2, 3).forEach { size ->
            repeat(50) {
                val letters = HillKey.randomLetters(size, random)

                assertThat(letters).hasLength(size * size)
                assertThat(HillKey.validate(letters, size)).isNull()
            }
        }
    }
}
