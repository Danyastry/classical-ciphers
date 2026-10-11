package com.danyastry.encryption.feature.bacon

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class BaconCipherTest {

    private val classic = BaconCipher(BaconAlphabet.CLASSIC_24)
    private val modern = BaconCipher(BaconAlphabet.MODERN_26)

    @Test
    fun `matches the wikipedia table for the classic alphabet`() {
        val table = mapOf(
            'A' to "AAAAA", 'B' to "AAAAB", 'H' to "AABBB", 'I' to "ABAAA", 'K' to "ABAAB",
            'L' to "ABABA", 'R' to "BAAAA", 'U' to "BAABB", 'W' to "BABAA", 'Z' to "BABBB",
        )

        table.forEach { (letter, code) -> assertThat(classic.encode(letter.toString())).isEqualTo(code) }
    }

    @Test
    fun `classic alphabet merges J with I and V with U`() {
        assertThat(classic.encode("J")).isEqualTo(classic.encode("I"))
        assertThat(classic.encode("V")).isEqualTo(classic.encode("U"))
        assertThat(classic.decode(classic.encode("JAVA"))).isEqualTo("IAUA")
    }

    @Test
    fun `modern alphabet gives every letter its own code`() {
        assertThat(modern.encode("JAVA")).isEqualTo("ABAAB AAAAA BABAB AAAAA")
        assertThat(modern.encode("Z")).isEqualTo("BBAAB")
        assertThat(('A'..'Z').map { modern.encode(it.toString()) }.toSet()).hasSize(26)
    }

    @Test
    fun `encodes words as groups of five`() {
        assertThat(classic.encode("Hello!")).isEqualTo("AABBB AABAA ABABA ABABA ABBAB")
    }

    @Test
    fun `decodes groups ignoring case spaces and other symbols`() {
        assertThat(classic.decode("aabbb AABAA ababa-ABABA abbab")).isEqualTo("HELLO")
    }

    @Test
    fun `unknown classic codes decode to a question mark`() {
        assertThat(classic.decode("BBBBB")).isEqualTo("?")
        assertThat(modern.decode("BBBBB")).isEqualTo("?")
    }

    @Test
    fun `rejects incomplete groups`() {
        assertThrows(IllegalArgumentException::class.java) { classic.decode("AABB") }
    }

    @Test
    fun `round trips the modern alphabet`() {
        val text = "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG"

        assertThat(modern.decode(modern.encode(text))).isEqualTo(text)
    }

    @Test
    fun `hides the message in the letter case of a cover text`() {
        assertThat(classic.hide("hi", "the quick brown fox jumps over the lazy dog")).isEqualTo("thE QUiCk br")
    }

    @Test
    fun `reveals the hidden message`() {
        val stegotext = classic.hide("attack at dawn", "the quick brown fox jumps over the lazy dog ".repeat(3))

        assertThat(classic.reveal(stegotext)).isEqualTo("ATTACKATDAWN")
    }

    @Test
    fun `hiding keeps punctuation and stops after the last used letter`() {
        val stegotext = classic.hide("a", "Hi, there! More text follows.")

        assertThat(stegotext).isEqualTo("hi, the")
    }

    @Test
    fun `counts the cover letters a message needs`() {
        assertThat(classic.requiredCoverLetters("hi")).isEqualTo(10)
        assertThat(classic.requiredCoverLetters("h i!")).isEqualTo(10)
        assertThat(classic.requiredCoverLetters("")).isEqualTo(0)
    }

    @Test
    fun `rejects cover text that is too short`() {
        assertThrows(IllegalArgumentException::class.java) { classic.hide("hello", "too short") }
    }

    @Test
    fun `handles empty text`() {
        assertThat(classic.encode("")).isEmpty()
        assertThat(classic.decode("")).isEmpty()
        assertThat(classic.reveal("")).isEmpty()
    }
}
