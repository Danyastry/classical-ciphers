package com.danyastry.encryption.feature.enigma

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class PlugboardTest {

    @Test
    fun `parses pairs ignoring case and separators`() {
        assertThat(Plugboard.parse("av, bs-cg").pairs).containsExactly("AV", "BS", "CG").inOrder()
    }

    @Test
    fun `swaps both letters of a pair and leaves others`() {
        val plugboard = Plugboard.parse("AV")

        assertThat(plugboard.swap(0)).isEqualTo('V' - 'A')
        assertThat(plugboard.swap('V' - 'A')).isEqualTo(0)
        assertThat(plugboard.swap(1)).isEqualTo(1)
    }

    @Test
    fun `validates the pairs`() {
        assertThat(Plugboard.validate("")).isNull()
        assertThat(Plugboard.validate("AV BS")).isNull()
        assertThat(Plugboard.validate("AVB")).isEqualTo(PlugboardError.ODD_LETTERS)
        assertThat(Plugboard.validate("AV AS")).isEqualTo(PlugboardError.REPEATED_LETTER)
        assertThat(Plugboard.validate("AA")).isEqualTo(PlugboardError.REPEATED_LETTER)
        assertThat(Plugboard.validate("ABCDEFGHIJKLMNOPQRSTUVWXYZ")).isNull()
    }

    @Test
    fun `rejects invalid pairs`() {
        assertThrows(IllegalArgumentException::class.java) { Plugboard.parse("AVB") }
    }
}
