package com.danyastry.encryption.core.classical

object LetterFrequency {

    val english: DoubleArray = doubleArrayOf(
        8.167, 1.492, 2.782, 4.253, 12.702, 2.228, 2.015, 6.094, 6.966, 0.153, 0.772, 4.025, 2.406,
        6.749, 7.507, 1.929, 0.095, 5.987, 6.327, 9.056, 2.758, 0.978, 2.360, 0.150, 1.974, 0.074,
    )

    val englishByFrequency: String = rankLetters(english)

    fun counts(text: String): IntArray = IntArray(ALPHABET_SIZE).also { counts ->
        text.forEach { char -> if (char.isAsciiLetter()) counts[char.uppercaseChar() - 'A']++ }
    }

    fun percentages(text: String): DoubleArray {
        val counts = counts(text)
        val total = counts.sum()
        return DoubleArray(ALPHABET_SIZE) { if (total == 0) 0.0 else counts[it] * 100.0 / total }
    }

    fun byFrequency(text: String): String = rankLetters(counts(text).map { it.toDouble() }.toDoubleArray())

    fun chiSquared(text: String): Double {
        val counts = counts(text)
        val total = counts.sum()
        if (total == 0) return Double.POSITIVE_INFINITY
        return counts.indices.sumOf { index ->
            val expected = total * english[index] / 100
            val difference = counts[index] - expected
            difference * difference / expected
        }
    }

    private fun rankLetters(weights: DoubleArray): String =
        ALPHABET.indices.sortedWith(compareByDescending<Int> { weights[it] }.thenBy { it }).map { ALPHABET[it] }.joinToString("")
}
