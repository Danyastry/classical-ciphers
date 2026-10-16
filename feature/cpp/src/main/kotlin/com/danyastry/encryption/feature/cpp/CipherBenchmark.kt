package com.danyastry.encryption.feature.cpp

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.cryptoapi.ByteCipher
import com.danyastry.encryption.core.cryptoapi.TextCipher

data class CipherSet(
    val caesar: TextCipher<Int>,
    val vigenere: TextCipher<Keyword>,
    val xor: ByteCipher<ByteArray>,
) {
    companion object {
        val Native = CipherSet(NativeCaesarCipher(), NativeVigenereCipher(), NativeXorCipher())
    }
}

enum class BenchmarkCipher { CAESAR, VIGENERE, XOR }

enum class InputSize(val bytes: Int) {
    SMALL(1_024),
    MEDIUM(64 * 1_024),
    LARGE(1_024 * 1_024),
}

enum class Verdict { CPP_FASTER, KOTLIN_FASTER, ABOUT_THE_SAME }

data class Comparison(val kotlinNanos: Long, val cppNanos: Long, val outputsMatch: Boolean) {

    val cppSpeedup: Double
        get() = kotlinNanos.toDouble() / cppNanos.coerceAtLeast(1)

    val verdict: Verdict
        get() = when {
            cppSpeedup >= 1 + SAME_SPEED_MARGIN -> Verdict.CPP_FASTER
            cppSpeedup <= 1 / (1 + SAME_SPEED_MARGIN) -> Verdict.KOTLIN_FASTER
            else -> Verdict.ABOUT_THE_SAME
        }

    private companion object {
        const val SAME_SPEED_MARGIN = 0.1
    }
}

class CipherBenchmark(
    private val clock: () -> Long = System::nanoTime,
    private val warmups: Int = WARMUPS,
    private val runs: Int = RUNS,
) {

    fun compare(cipher: BenchmarkCipher, size: InputSize, kotlin: CipherSet, cpp: CipherSet): Comparison {
        val text = sampleText(size.bytes)
        val bytes = text.encodeToByteArray()
        val (kotlinNanos, kotlinOutput) = measure { kotlin.run(cipher, text, bytes) }
        val (cppNanos, cppOutput) = measure { cpp.run(cipher, text, bytes) }
        return Comparison(kotlinNanos, cppNanos, sameOutput(kotlinOutput, cppOutput))
    }

    fun callOverhead(calls: Int, kotlin: CipherSet, cpp: CipherSet): Comparison {
        val text = sampleText(TINY_INPUT_CHARS)
        val (kotlinNanos, kotlinOutput) = measure { repeatCaesar(kotlin, text, calls) }
        val (cppNanos, cppOutput) = measure { repeatCaesar(cpp, text, calls) }
        return Comparison(kotlinNanos, cppNanos, kotlinOutput == cppOutput)
    }

    private fun repeatCaesar(set: CipherSet, text: String, calls: Int): String {
        var last = text
        repeat(calls) { last = set.caesar.encrypt(text, CAESAR_SHIFT) }
        return last
    }

    private fun CipherSet.run(cipher: BenchmarkCipher, text: String, bytes: ByteArray): Any = when (cipher) {
        BenchmarkCipher.CAESAR -> caesar.encrypt(text, CAESAR_SHIFT)
        BenchmarkCipher.VIGENERE -> vigenere.encrypt(text, VIGENERE_KEY)
        BenchmarkCipher.XOR -> xor.encrypt(bytes, XOR_KEY)
    }

    private fun <T> measure(operation: () -> T): Pair<Long, T> {
        repeat(warmups) { operation() }
        val samples = List(runs) {
            val start = clock()
            val output = operation()
            (clock() - start) to output
        }
        return samples.map { it.first }.sorted()[runs / 2] to samples.last().second
    }

    private fun sameOutput(a: Any, b: Any): Boolean = when {
        a is ByteArray && b is ByteArray -> a.contentEquals(b)
        else -> a == b
    }

    companion object {
        const val WARMUPS = 3
        const val RUNS = 7
        const val TINY_INPUT_CHARS = 16
        const val OVERHEAD_CALLS = 10_000
        const val CAESAR_SHIFT = 3
        val VIGENERE_KEY = Keyword("LEMON")
        val XOR_KEY = "a sixteen byte k".encodeToByteArray()
        private const val PANGRAM = "The quick brown fox jumps over the lazy dog. "

        fun sampleText(length: Int): String = PANGRAM.repeat(length / PANGRAM.length + 1).take(length)
    }
}
