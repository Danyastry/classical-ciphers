package com.danyastry.encryption.feature.autokey

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.testing.assertRoundTrip
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AutokeyCipherTest {

    private val cipher = AutokeyCipher()
    private val queenly = Keyword("QUEENLY")

    @Test
    fun `matches the reference example`() {
        assertThat(cipher.encrypt("ATTACKATDAWN", queenly)).isEqualTo("QNXEPVYTWTWP")
        assertThat(cipher.decrypt("QNXEPVYTWTWP", queenly)).isEqualTo("ATTACKATDAWN")
    }

    @Test
    fun `keystream is the primer followed by the plaintext`() {
        assertThat(cipher.keystream("attack at dawn", queenly)).isEqualTo("QUEENLYATTAC")
    }

    @Test
    fun `keystream is cut to the primer when the text is shorter`() {
        assertThat(cipher.keystream("hi", queenly)).isEqualTo("QU")
    }

    @Test
    fun `preserves case and skips non letters`() {
        assertThat(cipher.encrypt("attack at dawn", queenly)).isEqualTo("qnxepv yt wtwp")
        assertThat(cipher.encrypt("Hello, World!", Keyword("KEY"))).isEqualTo("Rijss, Hzfhr!")
    }

    @Test
    fun `key does not repeat like vigenere`() {
        assertThat(cipher.encrypt("AAAA", Keyword("B"))).isEqualTo("BAAA")
    }

    @Test
    fun `round trips different primers`() {
        listOf("A", "Z", "KEY", "QUEENLY", "ABCDEFGHIJKLMNOPQRSTUVWXYZ").forEach { primer ->
            assertRoundTrip(cipher, "The Quick Brown Fox Jumps Over The Lazy Dog, 123!", Keyword(primer))
        }
    }

    @Test
    fun `primer longer than the text still round trips`() {
        assertRoundTrip(cipher, "Hi!", Keyword("VERYLONGPRIMER"))
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.encrypt("", queenly)).isEmpty()
        assertThat(cipher.decrypt("", queenly)).isEmpty()
        assertThat(cipher.keystream("", queenly)).isEmpty()
    }
}
