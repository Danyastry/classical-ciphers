package com.danyastry.encryption.feature.caesar

import com.danyastry.encryption.core.testing.assertRoundTrip
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CaesarCipherTest {

    private val cipher = CaesarCipher()

    @Test
    fun `encrypts with the classic shift of three`() {
        assertThat(cipher.encrypt("HELLO", 3)).isEqualTo("KHOOR")
    }

    @Test
    fun `matches the reference pangram example`() {
        assertThat(cipher.encrypt("THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG", 23))
            .isEqualTo("QEB NRFZH YOLTK CLU GRJMP LSBO QEB IXWV ALD")
    }

    @Test
    fun `decrypts back to plaintext`() {
        assertThat(cipher.decrypt("KHOOR", 3)).isEqualTo("HELLO")
    }

    @Test
    fun `wraps around the end of the alphabet`() {
        assertThat(cipher.encrypt("xyz XYZ", 3)).isEqualTo("abc ABC")
    }

    @Test
    fun `preserves case`() {
        assertThat(cipher.encrypt("Hello World", 1)).isEqualTo("Ifmmp Xpsme")
    }

    @Test
    fun `leaves non letters untouched`() {
        assertThat(cipher.encrypt("a1, b2! c3? [d]", 1)).isEqualTo("b1, c2! d3? [e]")
    }

    @Test
    fun `shift of zero and of the alphabet size is identity`() {
        assertThat(cipher.encrypt("Caesar", 0)).isEqualTo("Caesar")
        assertThat(cipher.encrypt("Caesar", 26)).isEqualTo("Caesar")
    }

    @Test
    fun `normalizes large and negative shifts`() {
        assertThat(cipher.encrypt("abc", 29)).isEqualTo(cipher.encrypt("abc", 3))
        assertThat(cipher.encrypt("abc", -3)).isEqualTo(cipher.encrypt("abc", 23))
        assertThat(cipher.decrypt("abc", -3)).isEqualTo(cipher.encrypt("abc", 3))
    }

    @Test
    fun `rot13 is its own inverse`() {
        val text = "Why did the chicken cross the road?"

        assertThat(cipher.encrypt(cipher.encrypt(text, 13), 13)).isEqualTo(text)
    }

    @Test
    fun `round trips every shift and extreme keys`() {
        val keys = (-30..30) + listOf(Int.MIN_VALUE, Int.MAX_VALUE)

        keys.forEach { key -> assertRoundTrip(cipher, "The Quick Brown Fox, 123!", key) }
    }

    @Test
    fun `every shift maps the alphabet onto itself`() {
        val alphabet = ('a'..'z').joinToString("")

        (0 until CaesarCipher.ALPHABET_SIZE).forEach { shift ->
            val shifted = cipher.encrypt(alphabet, shift)
            assertThat(shifted.toSet()).isEqualTo(alphabet.toSet())
            assertThat(shifted.first()).isEqualTo('a' + shift)
        }
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.encrypt("", 5)).isEmpty()
        assertThat(cipher.decrypt("", 5)).isEmpty()
    }

    @Test
    fun `brute force tries every shift and finds the plaintext`() {
        val ciphertext = cipher.encrypt("attack at dawn", 7)

        val candidates = cipher.bruteForce(ciphertext)

        assertThat(candidates.map { it.shift }).containsExactlyElementsIn(0 until 26).inOrder()
        assertThat(candidates[7].plaintext).isEqualTo("attack at dawn")
        assertThat(candidates[0].plaintext).isEqualTo(ciphertext)
    }
}
