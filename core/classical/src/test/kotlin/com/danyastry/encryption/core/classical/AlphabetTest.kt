package com.danyastry.encryption.core.classical

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AlphabetTest {

    @Test
    fun `alphabet constants describe A to Z`() {
        assertThat(ALPHABET).isEqualTo(('A'..'Z').joinToString(""))
        assertThat(ALPHABET_SIZE).isEqualTo(ALPHABET.length)
    }

    @Test
    fun `recognizes ascii letters only`() {
        assertThat("aZmM".all { it.isAsciiLetter() }).isTrue()
        assertThat(" 1!\u00E9".any { it.isAsciiLetter() }).isFalse()
    }

    @Test
    fun `asciiLetters keeps only letters`() {
        assertThat("Hello, World! 42".asciiLetters()).isEqualTo("HelloWorld")
    }

    @Test
    fun `mapLetters passes zero based indexes and preserves case`() {
        assertThat("aBz".mapLetters { it }).isEqualTo("aBz")
        assertThat("aBz".mapLetters { it + 1 }).isEqualTo("bCa")
    }

    @Test
    fun `mapLetters wraps negative and large results`() {
        assertThat("abc".mapLetters { it - 1 }).isEqualTo("zab")
        assertThat("abc".mapLetters { it + 27 }).isEqualTo("bcd")
    }

    @Test
    fun `mapLetters leaves non letters untouched and skips them`() {
        val visited = mutableListOf<Int>()

        val result = "a, b! 1\u00E9".mapLetters { index -> visited += index; index }

        assertThat(result).isEqualTo("a, b! 1\u00E9")
        assertThat(visited).containsExactly(0, 1).inOrder()
    }
}
