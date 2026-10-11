package com.danyastry.encryption.core.classical

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class PolybiusSquareTest {

    @Test
    fun `plain square lists the alphabet without J`() {
        val square = PolybiusSquare.fromKeyword()

        assertThat(square.size).isEqualTo(5)
        assertThat(square.rows).containsExactly("ABCDE", "FGHIK", "LMNOP", "QRSTU", "VWXYZ").inOrder()
    }

    @Test
    fun `keyword letters come first without repeats`() {
        assertThat(PolybiusSquare.fromKeyword("playfair example").letters).isEqualTo("PLAYFIREXMBCDGHKNOQSTUVWZ")
        assertThat(PolybiusSquare.fromKeyword("BALLOON").letters).startsWith("BALON")
    }

    @Test
    fun `J becomes I in a 25 letter square`() {
        val square = PolybiusSquare.fromKeyword()

        assertThat(square.normalize("Jumping Jack 7!")).isEqualTo("IUMPINGIACK")
        assertThat('J' in square).isFalse()
    }

    @Test
    fun `alphanumeric square is 6x6 and keeps J and digits`() {
        val square = PolybiusSquare.fromKeyword("ph0qg64mea1yl2nofdxkr3cvs5zw7bj9uti8", PolybiusSquare.ALPHANUMERIC_36)

        assertThat(square.size).isEqualTo(6)
        assertThat(square.rows.first()).isEqualTo("PH0QG6")
        assertThat(square.normalize("Jump 42!")).isEqualTo("JUMP42")
    }

    @Test
    fun `finds rows and columns and wraps letter lookup`() {
        val square = PolybiusSquare.fromKeyword()

        assertThat(square.rowOf('H')).isEqualTo(1)
        assertThat(square.columnOf('H')).isEqualTo(2)
        assertThat(square.letterAt(1, 2)).isEqualTo('H')
        assertThat(square.letterAt(-1, 5)).isEqualTo('V')
    }

    @Test
    fun `rejects characters outside the square and bad alphabets`() {
        assertThrows(IllegalArgumentException::class.java) { PolybiusSquare.fromKeyword().rowOf('J') }
        assertThrows(IllegalArgumentException::class.java) { PolybiusSquare.fromKeyword(alphabet = "ABCDE") }
        assertThrows(IllegalArgumentException::class.java) { PolybiusSquare.fromKeyword(alphabet = "AABC") }
    }

    @Test
    fun `squares with the same letters are equal`() {
        assertThat(PolybiusSquare.fromKeyword("key")).isEqualTo(PolybiusSquare.fromKeyword("K-E-Y"))
    }
}
