package com.danyastry.encryption.feature.vigenere

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.testing.assertRoundTrip
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class VigenereCipherTest {

    private val cipher = VigenereCipher()
    private val lemon = Keyword("LEMON")

    @Test
    fun `matches the reference example`() {
        assertThat(cipher.encrypt("ATTACKATDAWN", lemon)).isEqualTo("LXFOPVEFRNHR")
        assertThat(cipher.decrypt("LXFOPVEFRNHR", lemon)).isEqualTo("ATTACKATDAWN")
    }

    @Test
    fun `key advances only on letters`() {
        assertThat(cipher.encrypt("attack at dawn", lemon)).isEqualTo("lxfopv ef rnhr")
    }

    @Test
    fun `preserves case and leaves non letters untouched`() {
        assertThat(cipher.encrypt("Hello, World!", Keyword("KEY"))).isEqualTo("Rijvs, Uyvjn!")
    }

    @Test
    fun `same letter encrypts differently depending on its position`() {
        val ciphertext = cipher.encrypt("AAAAA", lemon)

        assertThat(ciphertext).isEqualTo("LEMON")
    }

    @Test
    fun `keyword of A is identity`() {
        assertThat(cipher.encrypt("CRYPTOGRAPHY", Keyword("A"))).isEqualTo("CRYPTOGRAPHY")
    }

    @Test
    fun `single letter keyword is a caesar shift`() {
        assertThat(cipher.encrypt("Hello, World!", Keyword("B"))).isEqualTo("Ifmmp, Xpsme!")
    }

    @Test
    fun `round trips different keywords`() {
        listOf("A", "Z", "LEMON", "KEY", "CRYPTOGRAPHY", "ABCDEFGHIJKLMNOPQRSTUVWXYZ").forEach { keyword ->
            assertRoundTrip(cipher, "The Quick Brown Fox Jumps Over The Lazy Dog, 123!", Keyword(keyword))
        }
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.encrypt("", lemon)).isEmpty()
        assertThat(cipher.decrypt("", lemon)).isEmpty()
    }
}
