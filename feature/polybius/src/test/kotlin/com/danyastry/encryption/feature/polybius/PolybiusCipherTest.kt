package com.danyastry.encryption.feature.polybius

import com.danyastry.encryption.core.classical.PolybiusSquare
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class PolybiusCipherTest {

    private val cipher = PolybiusCipher()
    private val standard = PolybiusSquare.fromKeyword()

    @Test
    fun `matches the standard square examples`() {
        assertThat(cipher.encrypt("BAT", standard)).isEqualTo("12 11 44")
        assertThat(cipher.encrypt("HELLO", standard)).isEqualTo("23 15 31 31 34")
        assertThat(cipher.encrypt("WORLD", standard)).isEqualTo("52 34 42 31 14")
    }

    @Test
    fun `decodes digit pairs ignoring separators`() {
        assertThat(cipher.decrypt("23 15 31 31 34", standard)).isEqualTo("HELLO")
        assertThat(cipher.decrypt("2315313134", standard)).isEqualTo("HELLO")
        assertThat(cipher.decrypt("23-15/31,31.34", standard)).isEqualTo("HELLO")
    }

    @Test
    fun `merges J with I and drops non letters`() {
        assertThat(cipher.encrypt("Jo, 42!", standard)).isEqualTo("24 34")
        assertThat(cipher.decrypt("24 34", standard)).isEqualTo("IO")
    }

    @Test
    fun `keyword changes the square`() {
        val zebras = PolybiusSquare.fromKeyword("ZEBRAS")

        assertThat(zebras.rows).containsExactly("ZEBRA", "SCDFG", "HIKLM", "NOPQT", "UVWXY").inOrder()
        assertThat(cipher.encrypt("HELLO", zebras)).isEqualTo("31 12 34 34 42")
        assertThat(cipher.decrypt("31 12 34 34 42", zebras)).isEqualTo("HELLO")
    }

    @Test
    fun `validates codes`() {
        assertThat(cipher.isValidCode("23 15", standard)).isTrue()
        assertThat(cipher.isValidCode("", standard)).isTrue()
        assertThat(cipher.isValidCode("231", standard)).isFalse()
        assertThat(cipher.isValidCode("26", standard)).isFalse()
        assertThat(cipher.isValidCode("06", standard)).isFalse()
    }

    @Test
    fun `rejects invalid codes`() {
        assertThrows(IllegalArgumentException::class.java) { cipher.decrypt("231", standard) }
        assertThrows(IllegalArgumentException::class.java) { cipher.decrypt("69", standard) }
    }

    @Test
    fun `round trips the whole square`() {
        listOf("", "ZEBRAS", "PLAYFAIR EXAMPLE").forEach { keyword ->
            val square = PolybiusSquare.fromKeyword(keyword)
            assertThat(cipher.decrypt(cipher.encrypt(PolybiusSquare.LATIN_25, square), square))
                .isEqualTo(PolybiusSquare.LATIN_25)
        }
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.encrypt("", standard)).isEmpty()
        assertThat(cipher.decrypt("", standard)).isEmpty()
    }
}
