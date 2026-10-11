package com.danyastry.encryption.feature.onetimepad

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.testing.assertRoundTrip
import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Assert.assertThrows
import org.junit.Test

class OneTimePadCipherTest {

    private val cipher = OneTimePadCipher()

    @Test
    fun `matches the wikipedia example`() {
        assertThat(cipher.encrypt("HELLO", Keyword("XMCKL"))).isEqualTo("EQNVZ")
        assertThat(cipher.decrypt("EQNVZ", Keyword("XMCKL"))).isEqualTo("HELLO")
    }

    @Test
    fun `preserves case and skips non letters`() {
        assertThat(cipher.encrypt("He, llo!", Keyword("XMCKL"))).isEqualTo("Eq, nvz!")
    }

    @Test
    fun `key may be longer than the message`() {
        assertThat(cipher.encrypt("HELLO", Keyword("XMCKLABCDEF"))).isEqualTo("EQNVZ")
    }

    @Test
    fun `rejects keys shorter than the message`() {
        assertThrows(IllegalArgumentException::class.java) { cipher.encrypt("HELLO", Keyword("XMC")) }
        assertThrows(IllegalArgumentException::class.java) { cipher.decrypt("EQNVZ", Keyword("XMC")) }
    }

    @Test
    fun `finds a key for any plaintext of the same length`() {
        assertThat(cipher.keyFor("EQNVZ", "HELLO")).isEqualTo("XMCKL")
        assertThat(cipher.keyFor("EQNVZ", "LATER")).isEqualTo("TQURI")
        assertThat(cipher.decrypt("EQNVZ", Keyword("TQURI"))).isEqualTo("LATER")
    }

    @Test
    fun `every plaintext is reachable from one ciphertext`() {
        val ciphertext = "EQNVZ"

        listOf("HELLO", "LATER", "ABORT", "ZZZZZ", "AAAAA").forEach { guess ->
            val key = Keyword(cipher.keyFor(ciphertext, guess))
            assertThat(cipher.decrypt(ciphertext, key)).isEqualTo(guess)
        }
    }

    @Test
    fun `keyFor needs the same number of letters`() {
        assertThrows(IllegalArgumentException::class.java) { cipher.keyFor("EQNVZ", "HI") }
    }

    @Test
    fun `random keys have the requested length and only letters`() {
        val key = OneTimePadCipher.randomKey(64, Random(seed = 3))

        assertThat(key).hasLength(64)
        assertThat(key.all { it in 'A'..'Z' }).isTrue()
    }

    @Test
    fun `round trips random keys`() {
        val random = Random(seed = 8)
        val text = "The Quick Brown Fox Jumps Over The Lazy Dog, 42 times!"

        repeat(20) {
            assertRoundTrip(cipher, text, Keyword(OneTimePadCipher.randomKey(OneTimePadCipher.lettersIn(text), random)))
        }
    }
}
