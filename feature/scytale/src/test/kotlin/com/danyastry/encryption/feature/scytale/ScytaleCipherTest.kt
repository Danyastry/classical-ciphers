package com.danyastry.encryption.feature.scytale

import com.danyastry.encryption.core.testing.assertRoundTrip
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class ScytaleCipherTest {

    private val cipher = ScytaleCipher()

    @Test
    fun `matches the wikipedia example`() {
        assertThat(cipher.encrypt("Iamhurtverybadlyhelp", 4)).isEqualTo("Iryyatbhmvaehedlurlp")
        assertThat(cipher.decrypt("Iryyatbhmvaehedlurlp", 4)).isEqualTo("Iamhurtverybadlyhelp")
    }

    @Test
    fun `wraps the text along the rod`() {
        assertThat(cipher.wrap("Iamhurtverybadlyhelp", 4)).containsExactly("Iamhu", "rtver", "ybadl", "yhelp").inOrder()
    }

    @Test
    fun `last turn can be shorter`() {
        assertThat(cipher.wrap("ABCDEFG", 3)).containsExactly("ABC", "DEF", "G").inOrder()
        assertThat(cipher.encrypt("ABCDEFG", 3)).isEqualTo("ADGBECF")
        assertThat(cipher.decrypt("ADGBECF", 3)).isEqualTo("ABCDEFG")
    }

    @Test
    fun `letters along the rod is the length divided by turns rounded up`() {
        assertThat(cipher.lettersAlongRod(20, 4)).isEqualTo(5)
        assertThat(cipher.lettersAlongRod(21, 4)).isEqualTo(6)
        assertThat(cipher.lettersAlongRod(0, 4)).isEqualTo(0)
    }

    @Test
    fun `moves spaces and punctuation too`() {
        assertThat(cipher.encrypt("Hello, World!", 3)).isEqualTo("H,le dlW!loor")
    }

    @Test
    fun `one turn changes nothing`() {
        assertThat(cipher.encrypt("HELLO", 1)).isEqualTo("HELLO")
    }

    @Test
    fun `round trips many rod sizes and lengths`() {
        (1..12).forEach { turns ->
            (0..30).forEach { length ->
                assertRoundTrip(cipher, "abcdefghijklmnopqrstuvwxyz0123".take(length), turns)
            }
        }
    }

    @Test
    fun `rejects rods with no room for letters`() {
        assertThrows(IllegalArgumentException::class.java) { cipher.encrypt("HELLO", 0) }
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.encrypt("", 4)).isEmpty()
        assertThat(cipher.decrypt("", 4)).isEmpty()
        assertThat(cipher.wrap("", 4)).isEmpty()
    }
}
