package com.danyastry.encryption.feature.playfair

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class PlayfairKeyTest {

    @Test
    fun `builds the reference square`() {
        assertThat(PlayfairKey("playfair example").square).isEqualTo("PLAYFIREXMBCDGHKNOQSTUVWZ")
        assertThat(PlayfairKey("MONARCHY").square).isEqualTo("MONARCHYBDEFGIKLPQSTUVWXZ")
    }

    @Test
    fun `square has 25 distinct letters without J`() {
        listOf("", "JJJ", "playfair example", "The quick brown fox jumps over the lazy dog").forEach { keyword ->
            val square = PlayfairKey(keyword).square

            assertThat(square).hasLength(25)
            assertThat(square.toSet()).hasSize(25)
            assertThat(square).doesNotContain("J")
        }
    }

    @Test
    fun `empty keyword gives the plain square`() {
        assertThat(PlayfairKey("").square).isEqualTo("ABCDEFGHIKLMNOPQRSTUVWXYZ")
    }

    @Test
    fun `J in the keyword becomes I`() {
        assertThat(PlayfairKey("JAZZ").square).startsWith("IAZ")
    }

    @Test
    fun `finds rows and columns`() {
        val key = PlayfairKey("playfair example")

        assertThat(key.rowOf('P')).isEqualTo(0)
        assertThat(key.columnOf('P')).isEqualTo(0)
        assertThat(key.rowOf('Z')).isEqualTo(4)
        assertThat(key.columnOf('Z')).isEqualTo(4)
        assertThat(key.rowOf('E')).isEqualTo(1)
        assertThat(key.columnOf('E')).isEqualTo(2)
    }

    @Test
    fun `letterAt wraps around the edges`() {
        val key = PlayfairKey("playfair example")

        assertThat(key.letterAt(0, 5)).isEqualTo('P')
        assertThat(key.letterAt(-1, 0)).isEqualTo('T')
        assertThat(key.letterAt(5, -1)).isEqualTo('F')
    }

    @Test
    fun `rejects letters outside the square`() {
        assertThrows(IllegalArgumentException::class.java) { PlayfairKey("").rowOf('J') }
    }

    @Test
    fun `keys with the same square are equal`() {
        assertThat(PlayfairKey("playfair example")).isEqualTo(PlayfairKey("PLAYFAIR-EXAMPLE!"))
    }
}
