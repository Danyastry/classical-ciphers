package com.danyastry.encryption.feature.vigenere

import com.danyastry.encryption.core.classical.ALPHABET
import com.danyastry.encryption.core.classical.ALPHABET_SIZE
import com.danyastry.encryption.core.classical.LetterFrequency
import com.danyastry.encryption.core.classical.asciiLetters
import com.danyastry.encryption.core.classical.mapLetters

data class RepeatedSequence(val sequence: String, val distance: Int)

data class KasiskiResult(
    val repeats: List<RepeatedSequence>,
    val factorCounts: Map<Int, Int>,
    val coincidence: Map<Int, Double>,
    val keyLength: Int,
    val key: String,
)

object VigenereCracker {

    const val MAX_KEY_LENGTH = 12
    const val SEQUENCE_LENGTH = 3
    private const val LENGTH_THRESHOLD = 0.9

    fun crack(ciphertext: String, maxKeyLength: Int = MAX_KEY_LENGTH): KasiskiResult? {
        val letters = ciphertext.asciiLetters().uppercase()
        val longest = minOf(maxKeyLength, letters.length / 2)
        if (longest < 1) return null
        val repeats = repeats(letters)
        val coincidence = (1..longest).associateWith { averageCoincidence(letters, it) }
        val keyLength = likelyKeyLength(coincidence)
        return KasiskiResult(
            repeats = repeats,
            factorCounts = (2..longest).associateWith { length -> repeats.count { it.distance % length == 0 } },
            coincidence = coincidence,
            keyLength = keyLength,
            key = keyFor(letters, keyLength),
        )
    }

    fun repeats(letters: String): List<RepeatedSequence> =
        letters.windowed(SEQUENCE_LENGTH).withIndex()
            .groupBy({ it.value }, { it.index })
            .filterValues { it.size > 1 }
            .flatMap { (sequence, positions) -> positions.zipWithNext { a, b -> RepeatedSequence(sequence, b - a) } }
            .sortedWith(compareBy({ it.distance }, { it.sequence }))

    fun indexOfCoincidence(letters: String): Double {
        if (letters.length < 2) return 0.0
        val counts = LetterFrequency.counts(letters)
        return counts.sumOf { it.toDouble() * (it - 1) } / (letters.length.toDouble() * (letters.length - 1))
    }

    fun keyFor(letters: String, keyLength: Int): String = (0 until keyLength).map { column ->
        val columnLetters = letters.filterIndexed { index, _ -> index % keyLength == column }
        val shift = (0 until ALPHABET_SIZE).minBy { candidate ->
            LetterFrequency.chiSquared(columnLetters.mapLetters { it - candidate })
        }
        ALPHABET[shift]
    }.joinToString("")

    private fun averageCoincidence(letters: String, keyLength: Int): Double =
        (0 until keyLength).map { column ->
            indexOfCoincidence(letters.filterIndexed { index, _ -> index % keyLength == column })
        }.average()

    private fun likelyKeyLength(coincidence: Map<Int, Double>): Int {
        val best = coincidence.values.max()
        return coincidence.keys.sorted().first { coincidence.getValue(it) >= best * LENGTH_THRESHOLD }
    }
}
