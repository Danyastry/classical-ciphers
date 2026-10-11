package com.danyastry.encryption.feature.vigenere

import com.danyastry.encryption.core.classical.ALPHABET
import com.danyastry.encryption.core.classical.Keyword
import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Test

class VigenereCrackerTest {

    private val cipher = VigenereCipher()

    @Test
    fun `finds repeated sequences and their distances`() {
        val repeats = VigenereCracker.repeats("ABCXXABCYYABC")

        assertThat(repeats).containsExactly(
            RepeatedSequence("ABC", 5),
            RepeatedSequence("ABC", 5),
        )
    }

    @Test
    fun `no repeats in text without them`() {
        assertThat(VigenereCracker.repeats("ABCDEFGHIJ")).isEmpty()
    }

    @Test
    fun `index of coincidence separates english from random letters`() {
        val english = SampleText.KASISKI.uppercase().filter { it in 'A'..'Z' }
        val random = Random(seed = 1).let { r -> String(CharArray(english.length) { ALPHABET[r.nextInt(26)] }) }

        assertThat(VigenereCracker.indexOfCoincidence(english)).isGreaterThan(0.06)
        assertThat(VigenereCracker.indexOfCoincidence(random)).isLessThan(0.045)
    }

    @Test
    fun `cracks the lemon key from the sample`() {
        val result = requireNotNull(VigenereCracker.crack(cipher.encrypt(SampleText.KASISKI, Keyword("LEMON"))))

        assertThat(result.keyLength).isEqualTo(5)
        assertThat(result.key).isEqualTo("LEMON")
        assertThat(result.factorCounts.maxBy { it.value }.key).isEqualTo(5)
    }

    @Test
    fun `cracks known keys of many lengths`() {
        listOf("KEY", "LEMON", "CIPHER", "SECRET", "BABBAGE", "KASISKI", "QWERTYUI", "ABCDEFGHIJ").forEach { key ->
            val result = requireNotNull(VigenereCracker.crack(cipher.encrypt(SampleText.KASISKI, Keyword(key))))

            assertThat(result.key).isEqualTo(key)
        }
    }

    @Test
    fun `cracked key decrypts random keys back to the plaintext`() {
        val random = Random(seed = 1)
        val plaintext = SampleText.KASISKI

        repeat(50) {
            val secret = String(CharArray(random.nextInt(2, 11)) { ALPHABET[random.nextInt(26)] })
            val ciphertext = cipher.encrypt(plaintext, Keyword(secret))

            val cracked = requireNotNull(VigenereCracker.crack(ciphertext)).key

            assertThat(cipher.decrypt(ciphertext, Keyword(cracked))).isEqualTo(plaintext)
        }
    }

    @Test
    fun `short or empty text gives no result`() {
        assertThat(VigenereCracker.crack("")).isNull()
        assertThat(VigenereCracker.crack("A, 1!")).isNull()
    }

    @Test
    fun `key length search never exceeds half the text`() {
        val result = requireNotNull(VigenereCracker.crack("ABCDEFGH"))

        assertThat(result.coincidence.keys.max()).isEqualTo(4)
    }
}
