package com.danyastry.encryption.feature.bifid

import com.danyastry.encryption.core.classical.PolybiusSquare
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class BifidCipherTest {

    private val cipher = BifidCipher()
    private val wikipedia = BifidKey(PolybiusSquare.fromKeyword("BGWKZQPNDSIOAXEFCLUMTHYVR"))
    private val practical = BifidKey(PolybiusSquare.fromKeyword("phqgmeaylnofdxkrcvszwbuti"), period = 5)

    @Test
    fun `matches the wikipedia example`() {
        assertThat(cipher.encrypt("FLEEATONCE", wikipedia)).isEqualTo("UAEOLWRINS")
        assertThat(cipher.decrypt("UAEOLWRINS", wikipedia)).isEqualTo("FLEEATONCE")
    }

    @Test
    fun `matches the practical cryptography example with period five`() {
        assertThat(cipher.encrypt("defendtheeastwallofthecastle", practical)).isEqualTo("FFYHMKHYCPLIASHADTRLHCCHLBLR")
        assertThat(cipher.decrypt("FFYHMKHYCPLIASHADTRLHCCHLBLR", practical)).isEqualTo("DEFENDTHEEASTWALLOFTHECASTLE")
    }

    @Test
    fun `period changes the result`() {
        val text = "FLEEATONCE"

        assertThat(cipher.encrypt(text, wikipedia.copy(period = 5))).isNotEqualTo(cipher.encrypt(text, wikipedia))
    }

    @Test
    fun `period of one changes nothing`() {
        assertThat(cipher.encrypt("FLEEATONCE", wikipedia.copy(period = 1))).isEqualTo("FLEEATONCE")
    }

    @Test
    fun `keeps only letters and merges J with I`() {
        assertThat(cipher.encrypt("Flee, at once!", wikipedia)).isEqualTo("UAEOLWRINS")
        assertThat(cipher.decrypt(cipher.encrypt("JAZZ", wikipedia), wikipedia)).isEqualTo("IAZZ")
    }

    @Test
    fun `round trips different periods and lengths`() {
        val text = "THEQUICKBROWNFOXUMPSOVERTHELAZYDOG"

        (0..12).forEach { period ->
            (0..text.length).forEach { length ->
                val key = wikipedia.copy(period = period)
                assertThat(cipher.decrypt(cipher.encrypt(text.take(length), key), key)).isEqualTo(text.take(length))
            }
        }
    }

    @Test
    fun `rejects bad keys`() {
        assertThrows(IllegalArgumentException::class.java) { BifidKey(wikipedia.square, period = -1) }
        assertThrows(IllegalArgumentException::class.java) {
            BifidKey(PolybiusSquare.fromKeyword(alphabet = PolybiusSquare.ALPHANUMERIC_36))
        }
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.encrypt("", wikipedia)).isEmpty()
        assertThat(cipher.decrypt("", practical)).isEmpty()
    }
}
