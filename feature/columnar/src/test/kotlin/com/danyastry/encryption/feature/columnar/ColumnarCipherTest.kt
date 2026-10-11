package com.danyastry.encryption.feature.columnar

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.testing.assertRoundTrip
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ColumnarCipherTest {

    private val irregular = ColumnarCipher()
    private val padded = ColumnarCipher(padding = 'X')
    private val zebras = Keyword("ZEBRAS")

    @Test
    fun `reads columns in alphabetical keyword order`() {
        assertThat(ColumnarCipher.columnOrder(zebras)).containsExactly(4, 2, 1, 3, 5, 0).inOrder()
        assertThat(ColumnarCipher.columnOrder(Keyword("GERMAN"))).containsExactly(4, 1, 0, 3, 5, 2).inOrder()
    }

    @Test
    fun `repeated keyword letters keep their left to right order`() {
        assertThat(ColumnarCipher.columnOrder(Keyword("APPLE"))).containsExactly(0, 4, 3, 1, 2).inOrder()
        assertThat(irregular.encrypt("ABCDEFGHIJ", Keyword("APPLE"))).isEqualTo("AFEJDIBGCH")
    }

    @Test
    fun `matches the wikipedia irregular example`() {
        assertThat(irregular.encrypt("WEAREDISCOVEREDFLEEATONCE", zebras)).isEqualTo("EVLNACDTESEAROFODEECWIREE")
        assertThat(irregular.decrypt("EVLNACDTESEAROFODEECWIREE", zebras)).isEqualTo("WEAREDISCOVEREDFLEEATONCE")
    }

    @Test
    fun `matches the wikipedia example with explicit nulls`() {
        assertThat(irregular.encrypt("WEAREDISCOVEREDFLEEATONCEQKJEU", zebras))
            .isEqualTo("EVLNEACDTKESEAQROFOJDEECUWIREE")
    }

    @Test
    fun `matches the practical cryptography padded example`() {
        assertThat(padded.encrypt("DEFENDTHEEASTWALLOFTHECASTLE", Keyword("GERMAN")))
            .isEqualTo("NALCXEHWTTDTTFSEELEEDSOAXFEAHL")
    }

    @Test
    fun `padding fills the last row`() {
        assertThat(padded.padded("ABCDEFG", 3)).isEqualTo("ABCDEFGXX")
        assertThat(padded.padded("ABCDEF", 3)).isEqualTo("ABCDEF")
        assertThat(irregular.padded("ABCDEFG", 3)).isEqualTo("ABCDEFG")
    }

    @Test
    fun `decrypting padded ciphertext keeps the padding`() {
        val key = Keyword("GERMAN")

        assertThat(padded.decrypt("NALCXEHWTTDTTFSEELEEDSOAXFEAHL", key)).isEqualTo("DEFENDTHEEASTWALLOFTHECASTLEXX")
    }

    @Test
    fun `moves spaces and punctuation too`() {
        assertThat(irregular.encrypt("Hello, World!", Keyword("KEY"))).isEqualTo("eoWlHl r!l,od")
    }

    @Test
    fun `single letter keyword changes nothing`() {
        assertThat(irregular.encrypt("HELLO", Keyword("A"))).isEqualTo("HELLO")
    }

    @Test
    fun `round trips many keywords and lengths`() {
        listOf("A", "KEY", "ZEBRAS", "APPLE", "LONGKEYWORDHERE").forEach { keyword ->
            (0..30).forEach { length ->
                assertRoundTrip(irregular, "abcdefghijklmnopqrstuvwxyz0123".take(length), Keyword(keyword))
            }
        }
    }

    @Test
    fun `handles empty text`() {
        assertThat(irregular.encrypt("", zebras)).isEmpty()
        assertThat(irregular.decrypt("", zebras)).isEmpty()
        assertThat(padded.encrypt("", zebras)).isEmpty()
    }
}
