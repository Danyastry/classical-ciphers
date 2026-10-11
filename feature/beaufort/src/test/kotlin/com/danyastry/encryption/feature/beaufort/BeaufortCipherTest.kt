package com.danyastry.encryption.feature.beaufort

import com.danyastry.encryption.core.testing.assertRoundTrip
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class BeaufortCipherTest {

    private val cipher = BeaufortCipher()
    private val fortification = BeaufortKey("FORTIFICATION")

    @Test
    fun `matches the reference example`() {
        assertThat(cipher.encrypt("DEFENDTHEEASTWALLOFTHECASTLE", fortification))
            .isEqualTo("CKMPVCPVWPIWUJOGIUAPVWRIWUUK")
        assertThat(cipher.decrypt("CKMPVCPVWPIWUJOGIUAPVWRIWUUK", fortification))
            .isEqualTo("DEFENDTHEEASTWALLOFTHECASTLE")
    }

    @Test
    fun `preserves case and leaves non letters untouched`() {
        assertThat(cipher.encrypt("Hello, World!", BeaufortKey("KEY"))).isEqualTo("Danzq, Cwnnh!")
    }

    @Test
    fun `differs from vigenere for the same key`() {
        assertThat(cipher.encrypt("ATTACKATDAWN", BeaufortKey("LEMON"))).isEqualTo("LLTOLBETLNPR")
    }

    @Test
    fun `keyword of A mirrors the alphabet like atbash shifted by one`() {
        assertThat(cipher.encrypt("ABCXYZ", BeaufortKey("A"))).isEqualTo("AZYDCB")
    }

    @Test
    fun `encrypting twice gives the plaintext back`() {
        val text = "The Quick Brown Fox Jumps Over The Lazy Dog, 123!"

        listOf("A", "Z", "KEY", "FORTIFICATION").forEach { keyword ->
            val key = BeaufortKey(keyword)
            assertThat(cipher.encrypt(cipher.encrypt(text, key), key)).isEqualTo(text)
        }
    }

    @Test
    fun `encrypt and decrypt are the same operation`() {
        val text = "Attack at dawn"

        assertThat(cipher.decrypt(text, fortification)).isEqualTo(cipher.encrypt(text, fortification))
        assertRoundTrip(cipher, text, fortification)
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.encrypt("", fortification)).isEmpty()
    }
}
