package com.danyastry.encryption.feature.substitution

import com.danyastry.encryption.core.classical.ALPHABET
import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Assert.assertThrows
import org.junit.Test

class SubstitutionKeyTest {

    @Test
    fun `builds the key from a keyword`() {
        assertThat(SubstitutionKey.fromKeyword("ZEBRAS").alphabet).isEqualTo("ZEBRASCDFGHIJKLMNOPQTUVWXY")
    }

    @Test
    fun `keyword ignores case, repeated letters and non letters`() {
        assertThat(SubstitutionKey.fromKeyword("ze-bra s!").alphabet).isEqualTo("ZEBRASCDFGHIJKLMNOPQTUVWXY")
        assertThat(SubstitutionKey.fromKeyword("BALLOON").alphabet).isEqualTo("BALONCDEFGHIJKMPQRSTUVWXYZ")
    }

    @Test
    fun `empty keyword gives the plain alphabet`() {
        assertThat(SubstitutionKey.fromKeyword("").alphabet).isEqualTo(ALPHABET)
    }

    @Test
    fun `validates the key alphabet`() {
        assertThat(SubstitutionKey.validate("ZEBRASCDFGHIJKLMNOPQTUVWXY")).isNull()
        assertThat(SubstitutionKey.validate("ZEBRAS")).isEqualTo(KeyError.WRONG_LENGTH)
        assertThat(SubstitutionKey.validate("")).isEqualTo(KeyError.WRONG_LENGTH)
        assertThat(SubstitutionKey.validate("zebrascdfghijklmnopqtuvwxy")).isEqualTo(KeyError.INVALID_CHARACTERS)
        assertThat(SubstitutionKey.validate("ZEBRAS1DFGHIJKLMNOPQTUVWXY")).isEqualTo(KeyError.INVALID_CHARACTERS)
        assertThat(SubstitutionKey.validate("AABCDEFGHIJKLMNOPQRSTUVWXY")).isEqualTo(KeyError.DUPLICATE_LETTERS)
    }

    @Test
    fun `rejects invalid keys`() {
        listOf("", "ZEBRAS", "AABCDEFGHIJKLMNOPQRSTUVWXY", "zebrascdfghijklmnopqtuvwxy").forEach { alphabet ->
            assertThrows(IllegalArgumentException::class.java) { SubstitutionKey(alphabet) }
        }
    }

    @Test
    fun `random keys are valid permutations`() {
        val random = Random(seed = 42)

        repeat(100) {
            val key = SubstitutionKey.random(random)
            assertThat(key.alphabet.toSet()).isEqualTo(ALPHABET.toSet())
        }
    }

    @Test
    fun `random keys differ from each other`() {
        val random = Random(seed = 7)

        val keys = List(20) { SubstitutionKey.random(random).alphabet }

        assertThat(keys.toSet().size).isGreaterThan(1)
    }
}
