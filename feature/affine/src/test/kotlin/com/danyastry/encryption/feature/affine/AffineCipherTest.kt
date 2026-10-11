package com.danyastry.encryption.feature.affine

import com.danyastry.encryption.core.testing.assertRoundTrip
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AffineCipherTest {

    private val cipher = AffineCipher()

    @Test
    fun `matches the reference example`() {
        val key = AffineKey(a = 5, b = 8)

        assertThat(cipher.encrypt("AFFINE CIPHER", key)).isEqualTo("IHHWVC SWFRCP")
        assertThat(cipher.decrypt("IHHWVC SWFRCP", key)).isEqualTo("AFFINE CIPHER")
    }

    @Test
    fun `preserves case and leaves non letters untouched`() {
        assertThat(cipher.encrypt("Hello, World!", AffineKey(a = 7, b = 3))).isEqualTo("Afccx, Bxscy!")
    }

    @Test
    fun `a of one is the caesar cipher`() {
        assertThat(cipher.encrypt("HELLO", AffineKey(a = 1, b = 3))).isEqualTo("KHOOR")
    }

    @Test
    fun `a and b of twenty five is the atbash cipher`() {
        assertThat(cipher.encrypt("abcdefghijklmnopqrstuvwxyz", AffineKey(a = 25, b = 25)))
            .isEqualTo("zyxwvutsrqponmlkjihgfedcba")
    }

    @Test
    fun `every valid key maps the alphabet onto itself`() {
        val alphabet = ('a'..'z').joinToString("")

        AffineKey.VALID_MULTIPLIERS.forEach { a ->
            (0 until 26).forEach { b ->
                assertThat(cipher.encrypt(alphabet, AffineKey(a, b)).toSet()).isEqualTo(alphabet.toSet())
            }
        }
    }

    @Test
    fun `round trips every valid key`() {
        AffineKey.VALID_MULTIPLIERS.forEach { a ->
            (0 until 26).forEach { b ->
                assertRoundTrip(cipher, "The Quick Brown Fox, 123!", AffineKey(a, b))
            }
        }
    }

    @Test
    fun `normalizes large and negative key values`() {
        val text = "Normalize me"

        assertThat(cipher.encrypt(text, AffineKey(a = 31, b = 34))).isEqualTo(cipher.encrypt(text, AffineKey(a = 5, b = 8)))
        assertThat(cipher.encrypt(text, AffineKey(a = -21, b = -18))).isEqualTo(cipher.encrypt(text, AffineKey(a = 5, b = 8)))
        assertRoundTrip(cipher, text, AffineKey(a = Int.MAX_VALUE, b = Int.MIN_VALUE))
    }

    @Test
    fun `handles empty text`() {
        val key = AffineKey(a = 5, b = 8)

        assertThat(cipher.encrypt("", key)).isEmpty()
        assertThat(cipher.decrypt("", key)).isEmpty()
    }
}
