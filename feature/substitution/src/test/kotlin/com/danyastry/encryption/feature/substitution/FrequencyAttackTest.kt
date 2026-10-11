package com.danyastry.encryption.feature.substitution

import com.danyastry.encryption.core.classical.ALPHABET
import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Assert.assertThrows
import org.junit.Test

class FrequencyAttackTest {

    private val cipher = SubstitutionCipher()

    @Test
    fun `guessed key is a valid permutation`() {
        val key = FrequencyAttack.guessKey("Some ciphertext without much meaning")

        assertThat(key.alphabet.toSet()).isEqualTo(ALPHABET.toSet())
    }

    @Test
    fun `the most common cipher letters decrypt to E and T`() {
        val random = Random(seed = 21)

        repeat(10) {
            val secret = SubstitutionKey.random(random)
            val ciphertext = cipher.encrypt(SampleText.FREQUENCY_ANALYSIS, secret)

            val guess = FrequencyAttack.guessKey(ciphertext)

            assertThat(cipher.decrypt(secret.alphabet[4].toString(), guess)).isEqualTo("E")
            assertThat(cipher.decrypt(secret.alphabet[19].toString(), guess)).isEqualTo("T")
        }
    }

    @Test
    fun `guess on plain english keeps the most common letters in place`() {
        val guess = FrequencyAttack.guessKey(SampleText.FREQUENCY_ANALYSIS)

        assertThat(cipher.decrypt("ET", guess)).isEqualTo("ET")
    }

    @Test
    fun `swapping plaintext letters swaps them in the decryption`() {
        val key = SubstitutionKey.fromKeyword("ZEBRAS")
        val ciphertext = cipher.encrypt("HELLO", key)

        val swapped = FrequencyAttack.swapPlaintextLetters(key, 'l', 'O')

        assertThat(cipher.decrypt(ciphertext, swapped)).isEqualTo("HEOOL")
    }

    @Test
    fun `swapping twice restores the key`() {
        val key = SubstitutionKey.fromKeyword("ZEBRAS")

        val restored = FrequencyAttack.swapPlaintextLetters(FrequencyAttack.swapPlaintextLetters(key, 'A', 'Q'), 'A', 'Q')

        assertThat(restored).isEqualTo(key)
    }

    @Test
    fun `rejects non letters`() {
        assertThrows(IllegalArgumentException::class.java) {
            FrequencyAttack.swapPlaintextLetters(SubstitutionKey(ALPHABET), '1', 'A')
        }
    }

    @Test
    fun `sample text is long enough for frequency analysis`() {
        assertThat(SampleText.FREQUENCY_ANALYSIS.count { it.isLetter() }).isAtLeast(500)
    }
}
