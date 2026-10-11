package com.danyastry.encryption.feature.playfair

import com.danyastry.encryption.core.classical.PolybiusSquare
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class PlayfairCipherTest {

    private val cipher = PlayfairCipher()
    private val example = PolybiusSquare.fromKeyword("playfair example")

    @Test
    fun `builds the reference squares`() {
        assertThat(example.letters).isEqualTo("PLAYFIREXMBCDGHKNOQSTUVWZ")
        assertThat(PolybiusSquare.fromKeyword("MONARCHY").letters).isEqualTo("MONARCHYBDEFGIKLPQSTUVWXZ")
    }

    @Test
    fun `every keyword gives 25 distinct letters without J`() {
        listOf("", "JJJ", "playfair example", "The quick brown fox jumps over the lazy dog").forEach { keyword ->
            val letters = PolybiusSquare.fromKeyword(keyword).letters

            assertThat(letters).hasLength(25)
            assertThat(letters.toSet()).hasSize(25)
            assertThat(letters).doesNotContain("J")
        }
    }

    @Test
    fun `rejects squares that are not the 25 letter latin square`() {
        val alphanumeric = PolybiusSquare.fromKeyword(alphabet = PolybiusSquare.ALPHANUMERIC_36)

        assertThrows(IllegalArgumentException::class.java) { cipher.encrypt("HELLO", alphanumeric) }
    }

    @Test
    fun `matches the wikipedia example`() {
        assertThat(cipher.encrypt("hide the gold in the tree stump", example))
            .isEqualTo("BMODZBXDNABEKUDMUIXMMOUVIF")
    }

    @Test
    fun `matches the textbook example`() {
        assertThat(cipher.encrypt("balloon", PolybiusSquare.fromKeyword("monarchy"))).isEqualTo("IBSUPMNA")
    }

    @Test
    fun `decrypts back to the prepared plaintext with fillers`() {
        assertThat(cipher.decrypt("BMODZBXDNABEKUDMUIXMMOUVIF", example)).isEqualTo("HIDETHEGOLDINTHETREXESTUMP")
        assertThat(cipher.decrypt("IB SU PM NA", PolybiusSquare.fromKeyword("monarchy"))).isEqualTo("BALXLOON")
    }

    @Test
    fun `splits plaintext into digraphs`() {
        assertThat(cipher.digraphs("hide the gold in the tree stump")).containsExactly(
            "HI", "DE", "TH", "EG", "OL", "DI", "NT", "HE", "TR", "EX", "ES", "TU", "MP",
        ).inOrder()
    }

    @Test
    fun `inserts X between doubled letters and pads odd length`() {
        assertThat(cipher.digraphs("balloon")).containsExactly("BA", "LX", "LO", "ON").inOrder()
        assertThat(cipher.digraphs("a")).containsExactly("AX")
    }

    @Test
    fun `uses Q as the filler for X`() {
        assertThat(cipher.digraphs("xx")).containsExactly("XQ", "XQ").inOrder()
        assertThat(cipher.digraphs("ax x")).containsExactly("AX", "XQ").inOrder()
    }

    @Test
    fun `treats J as I and ignores non letters`() {
        assertThat(cipher.digraphs("Jazz 42!")).containsExactly("IA", "ZX", "ZX").inOrder()
    }

    @Test
    fun `reports which rule each pair used`() {
        val steps = cipher.encryptSteps("hide the g", example)

        assertThat(steps.map { it.rule }).containsExactly(
            PlayfairRule.RECTANGLE,
            PlayfairRule.SAME_COLUMN,
            PlayfairRule.RECTANGLE,
            PlayfairRule.RECTANGLE,
        ).inOrder()
        assertThat(steps.map { it.output }).containsExactly("BM", "OD", "ZB", "XD").inOrder()
    }

    @Test
    fun `same row shifts right and wraps`() {
        val step = cipher.encryptSteps("PF", example).single()

        assertThat(step.rule).isEqualTo(PlayfairRule.SAME_ROW)
        assertThat(step.output).isEqualTo("LP")
    }

    @Test
    fun `same column shifts down and wraps`() {
        val step = cipher.encryptSteps("PT", example).single()

        assertThat(step.rule).isEqualTo(PlayfairRule.SAME_COLUMN)
        assertThat(step.output).isEqualTo("IP")
    }

    @Test
    fun `decrypting reverses every rule`() {
        val plaintext = "PFPTHIDETHEGOLD"
        val prepared = cipher.digraphs(plaintext).joinToString("")

        assertThat(cipher.decrypt(cipher.encrypt(plaintext, example), example)).isEqualTo(prepared)
    }

    @Test
    fun `round trips text that needs no fillers`() {
        val text = "ATTACKATDAWN".let { cipher.digraphs(it).joinToString("") }

        listOf("", "monarchy", "playfair example", "keyword").forEach { keyword ->
            val key = PolybiusSquare.fromKeyword(keyword)
            assertThat(cipher.decrypt(cipher.encrypt(text, key), key)).isEqualTo(text)
        }
    }

    @Test
    fun `rejects ciphertext with an odd number of letters`() {
        assertThrows(IllegalArgumentException::class.java) { cipher.decrypt("ABC", example) }
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.encrypt("", example)).isEmpty()
        assertThat(cipher.decrypt("", example)).isEmpty()
    }
}
