package com.danyastry.encryption.feature.trifid

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class TrifidCubeTest {

    private val wikipedia = TrifidCube.fromKeyword("FELIX MARIE DELASTELLE")

    @Test
    fun `builds the wikipedia cube`() {
        assertThat(wikipedia.letters).isEqualTo("FELIXMARDSTBCGHJKNOPQUVWYZ+")
        assertThat(wikipedia.layers).containsExactly(
            listOf("FEL", "IXM", "ARD"),
            listOf("STB", "CGH", "JKN"),
            listOf("OPQ", "UVW", "YZ+"),
        ).inOrder()
    }

    @Test
    fun `cube holds every letter, J and plus exactly once`() {
        listOf("", "JJJ", "FELIX MARIE DELASTELLE", "the quick brown fox").forEach { keyword ->
            val letters = TrifidCube.fromKeyword(keyword).letters

            assertThat(letters).hasLength(27)
            assertThat(letters.toSet()).isEqualTo(TrifidCube.CUBE_ALPHABET.toSet())
        }
    }

    @Test
    fun `finds layer, row and column`() {
        assertThat(wikipedia.coordinatesOf('F')).isEqualTo(Triple(0, 0, 0))
        assertThat(wikipedia.coordinatesOf('M')).isEqualTo(Triple(0, 1, 2))
        assertThat(wikipedia.coordinatesOf('+')).isEqualTo(Triple(2, 2, 2))
        assertThat(wikipedia.letterAt(1, 2, 0)).isEqualTo('J')
    }

    @Test
    fun `normalize keeps letters and plus`() {
        assertThat(TrifidCube.normalize("Aide-toi, le ciel t'aidera +1")).isEqualTo("AIDETOILECIELTAIDERA+")
    }

    @Test
    fun `rejects characters outside the cube`() {
        assertThrows(IllegalArgumentException::class.java) { wikipedia.coordinatesOf('.') }
    }
}
