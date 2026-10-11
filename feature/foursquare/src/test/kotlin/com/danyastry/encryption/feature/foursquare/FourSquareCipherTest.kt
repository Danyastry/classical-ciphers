package com.danyastry.encryption.feature.foursquare

import com.danyastry.encryption.core.classical.PolybiusSquare
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class FourSquareCipherTest {

    private val cipher = FourSquareCipher()
    private val wikipedia = FourSquareKey.fromKeywords("EXAMPLE", "KEYWORD", SquareAlphabet.OMIT_Q)
    private val practical = FourSquareKey.fromKeywords(
        "zgptfoihmuwdrcnykeqaxvsbl",
        "mfnbdcrhsaxyogvituewlqzkp",
        SquareAlphabet.MERGE_I_J,
    )

    @Test
    fun `matches the wikipedia example without Q`() {
        assertThat(wikipedia.upperRight.rows).containsExactly("EXAMP", "LBCDF", "GHIJK", "NORST", "UVWYZ").inOrder()
        assertThat(cipher.encrypt("help me obi wan kenobi", wikipedia)).isEqualTo("FYGMKYHOBXMFKKKIMD")
        assertThat(cipher.decrypt("FYGMKYHOBXMFKKKIMD", wikipedia)).isEqualTo("HELPMEOBIWANKENOBI")
    }

    @Test
    fun `matches the practical cryptography example with I and J merged`() {
        assertThat(cipher.encrypt("attack at dawn", practical)).isEqualTo("TIYBFHTIZBSY")
        assertThat(cipher.decrypt("TI YB FH TI ZB SY", practical)).isEqualTo("ATTACKATDAWN")
    }

    @Test
    fun `plain squares follow the chosen alphabet`() {
        assertThat(wikipedia.plain.letters).isEqualTo(PolybiusSquare.LATIN_25_NO_Q)
        assertThat(practical.plain.letters).isEqualTo(PolybiusSquare.LATIN_25)
    }

    @Test
    fun `pads an odd last letter with X`() {
        assertThat(cipher.digraphs("hello", wikipedia)).containsExactly("HE", "LL", "OX").inOrder()
        assertThat(cipher.decrypt(cipher.encrypt("hello", wikipedia), wikipedia)).isEqualTo("HELLOX")
    }

    @Test
    fun `doubled letters need no filler`() {
        assertThat(cipher.digraphs("balloon", practical)).containsExactly("BA", "LL", "OO", "NX").inOrder()
    }

    @Test
    fun `round trips both alphabets with different keywords`() {
        val text = "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOGS"

        SquareAlphabet.entries.forEach { alphabet ->
            listOf("" to "", "EXAMPLE" to "KEYWORD", "ZEBRAS" to "MONARCHY").forEach { (first, second) ->
                val key = FourSquareKey.fromKeywords(first, second, alphabet)
                val prepared = cipher.digraphs(text, key).joinToString("")
                assertThat(cipher.decrypt(cipher.encrypt(text, key), key)).isEqualTo(prepared)
            }
        }
    }

    @Test
    fun `rejects odd ciphertext and mismatched squares`() {
        assertThrows(IllegalArgumentException::class.java) { cipher.decrypt("ABC", wikipedia) }
        assertThrows(IllegalArgumentException::class.java) {
            FourSquareKey(
                upperRight = PolybiusSquare.fromKeyword("KEY", PolybiusSquare.LATIN_25),
                lowerLeft = PolybiusSquare.fromKeyword("KEY", PolybiusSquare.LATIN_25_NO_Q),
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            val square = PolybiusSquare.fromKeyword(alphabet = PolybiusSquare.ALPHANUMERIC_36)
            FourSquareKey(upperRight = square, lowerLeft = square)
        }
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.encrypt("", wikipedia)).isEmpty()
        assertThat(cipher.decrypt("", wikipedia)).isEmpty()
    }
}
