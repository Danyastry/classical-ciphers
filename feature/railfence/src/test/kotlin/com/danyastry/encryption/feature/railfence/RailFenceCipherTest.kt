package com.danyastry.encryption.feature.railfence

import com.danyastry.encryption.core.testing.assertRoundTrip
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class RailFenceCipherTest {

    private val cipher = RailFenceCipher()

    @Test
    fun `matches the wikipedia example`() {
        assertThat(cipher.encrypt("WEAREDISCOVEREDRUNATONCE", 3)).isEqualTo("WECRUOERDSOEERNTNEAIVDAC")
        assertThat(cipher.decrypt("WECRUOERDSOEERNTNEAIVDAC", 3)).isEqualTo("WEAREDISCOVEREDRUNATONCE")
    }

    @Test
    fun `matches the practical cryptography example`() {
        assertThat(cipher.encrypt("WEAREDISCOVEREDFLEEATONCE", 3)).isEqualTo("WECRLTEERDSOEEFEAOCAIVDEN")
    }

    @Test
    fun `moves spaces and punctuation too`() {
        assertThat(cipher.encrypt("Hello, World!", 2)).isEqualTo("Hlo ol!el,Wrd")
        assertThat(cipher.encrypt("Hello, World!", 4)).isEqualTo("H !e,Wdloollr")
    }

    @Test
    fun `rail pattern zigzags down and up`() {
        assertThat(cipher.railPattern(10, 3)).containsExactly(0, 1, 2, 1, 0, 1, 2, 1, 0, 1).inOrder()
        assertThat(cipher.railPattern(6, 2)).containsExactly(0, 1, 0, 1, 0, 1).inOrder()
        assertThat(cipher.railPattern(4, 1)).containsExactly(0, 0, 0, 0).inOrder()
    }

    @Test
    fun `one rail or more rails than letters changes nothing`() {
        assertThat(cipher.encrypt("HELLO", 1)).isEqualTo("HELLO")
        assertThat(cipher.encrypt("ABC", 5)).isEqualTo("ABC")
    }

    @Test
    fun `keeps every character exactly once`() {
        val text = "The Quick Brown Fox, 123!"

        (1..12).forEach { rails ->
            assertThat(cipher.encrypt(text, rails).toList().sorted()).isEqualTo(text.toList().sorted())
        }
    }

    @Test
    fun `round trips many rail counts and lengths`() {
        (1..12).forEach { rails ->
            (0..30).forEach { length ->
                assertRoundTrip(cipher, "abcdefghijklmnopqrstuvwxyz0123456789".take(length), rails)
            }
        }
    }

    @Test
    fun `rejects zero or negative rails`() {
        assertThrows(IllegalArgumentException::class.java) { cipher.encrypt("HELLO", 0) }
        assertThrows(IllegalArgumentException::class.java) { cipher.decrypt("HELLO", -1) }
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.encrypt("", 3)).isEmpty()
        assertThat(cipher.decrypt("", 3)).isEmpty()
    }
}
