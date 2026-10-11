package com.danyastry.encryption.core.classical

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LetterFrequencyTest {

    @Test
    fun `english frequencies cover the alphabet and add up to about 100 percent`() {
        assertThat(LetterFrequency.english.size).isEqualTo(26)
        assertThat(LetterFrequency.english.sum()).isWithin(0.1).of(100.0)
    }

    @Test
    fun `english letters ranked by frequency start with ETAOIN`() {
        assertThat(LetterFrequency.englishByFrequency).isEqualTo("ETAOINSHRDLCUMWFGYPBVKJXQZ")
    }

    @Test
    fun `counts letters ignoring case and other characters`() {
        val counts = LetterFrequency.counts("Aa b, C!")

        assertThat(counts[0]).isEqualTo(2)
        assertThat(counts[1]).isEqualTo(1)
        assertThat(counts[2]).isEqualTo(1)
        assertThat(counts.sum()).isEqualTo(4)
    }

    @Test
    fun `percentages add up to 100`() {
        assertThat(LetterFrequency.percentages("Hello, World").sum()).isWithin(1e-9).of(100.0)
        assertThat(LetterFrequency.percentages("123").sum()).isEqualTo(0.0)
    }

    @Test
    fun `ranks text letters by count and breaks ties alphabetically`() {
        assertThat(LetterFrequency.byFrequency("ccc bb a").take(4)).isEqualTo("CBAD")
    }

    @Test
    fun `english text scores better than shifted text`() {
        val english = "Frequency analysis works because some letters appear much more often than others"
        val shifted = english.mapLetters { it + 7 }

        assertThat(LetterFrequency.chiSquared(english)).isLessThan(LetterFrequency.chiSquared(shifted))
    }

    @Test
    fun `text without letters scores as infinitely unlikely`() {
        assertThat(LetterFrequency.chiSquared("123 !?")).isEqualTo(Double.POSITIVE_INFINITY)
    }
}
