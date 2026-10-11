package com.danyastry.encryption.feature.twosquare

import com.danyastry.encryption.core.classical.PolybiusSquare
import com.danyastry.encryption.core.testing.assertRoundTrip
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class TwoSquareCipherTest {

    private val cipher = TwoSquareCipher()
    private val wikipedia = TwoSquareKey.fromKeywords("EXAMPLE", "KEYWORD", SquareAlphabet.OMIT_Q)

    @Test
    fun `builds the wikipedia squares`() {
        assertThat(wikipedia.top.rows).containsExactly("EXAMP", "LBCDF", "GHIJK", "NORST", "UVWYZ").inOrder()
        assertThat(wikipedia.bottom.rows).containsExactly("KEYWO", "RDABC", "FGHIJ", "LMNPS", "TUVXZ").inOrder()
    }

    @Test
    fun `matches the wikipedia example`() {
        assertThat(cipher.encrypt("help me obi wan kenobi", wikipedia)).isEqualTo("HEDLXWSDJYANHOTKDG")
        assertThat(cipher.decrypt("HEDLXWSDJYANHOTKDG", wikipedia)).isEqualTo("HELPMEOBIWANKENOBI")
    }

    @Test
    fun `pairs sharing a column pass through unchanged`() {
        val steps = cipher.steps("help me obi wan kenobi", wikipedia)

        assertThat(steps.filter { it.isTransparency }.map { it.input }).containsExactly("HE", "AN").inOrder()
        assertThat(steps.first { it.input == "LP" }.output).isEqualTo("DL")
    }

    @Test
    fun `encrypting twice gives the text back for every pair`() {
        SquareAlphabet.entries.forEach { alphabet ->
            val key = TwoSquareKey.fromKeywords("EXAMPLE", "KEYWORD", alphabet)
            alphabet.letters.forEach { first ->
                alphabet.letters.forEach { second ->
                    val pair = "$first$second"
                    assertThat(cipher.encrypt(cipher.encrypt(pair, key), key)).isEqualTo(pair)
                }
            }
        }
    }

    @Test
    fun `decrypt is the same operation`() {
        assertRoundTrip(cipher, "ATTACKATDAWN", TwoSquareKey.fromKeywords("ZEBRAS", "MONARCHY"))
        assertThat(cipher.decrypt("ATTACK", wikipedia)).isEqualTo(cipher.encrypt("ATTACK", wikipedia))
    }

    @Test
    fun `pads an odd last letter with X`() {
        assertThat(cipher.digraphs("hello", wikipedia)).containsExactly("HE", "LL", "OX").inOrder()
    }

    @Test
    fun `rejects odd ciphertext and mismatched squares`() {
        assertThrows(IllegalArgumentException::class.java) { cipher.decrypt("ABC", wikipedia) }
        assertThrows(IllegalArgumentException::class.java) {
            TwoSquareKey(
                top = PolybiusSquare.fromKeyword("KEY", PolybiusSquare.LATIN_25),
                bottom = PolybiusSquare.fromKeyword("KEY", PolybiusSquare.LATIN_25_NO_Q),
            )
        }
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.encrypt("", wikipedia)).isEmpty()
        assertThat(cipher.steps("", wikipedia)).isEmpty()
    }
}
