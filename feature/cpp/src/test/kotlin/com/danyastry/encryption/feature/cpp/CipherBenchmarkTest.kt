package com.danyastry.encryption.feature.cpp

import com.danyastry.encryption.core.cryptoapi.TextCipher
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CipherBenchmarkTest {

    @Test
    fun `reports the median of the measured runs`() {
        val durations = listOf(50L, 10L, 30L, 20L, 40L)
        val benchmark = CipherBenchmark(clock = scriptedClock(durations + durations.map { it * 2 }), warmups = 0, runs = 5)

        val comparison = benchmark.compare(BenchmarkCipher.CAESAR, InputSize.SMALL, FakeCiphers.reference(), FakeCiphers.reference())

        assertThat(comparison.kotlinNanos).isEqualTo(30)
        assertThat(comparison.cppNanos).isEqualTo(60)
    }

    @Test
    fun `speedup compares kotlin time to c++ time`() {
        assertThat(Comparison(kotlinNanos = 400, cppNanos = 100, outputsMatch = true).cppSpeedup).isEqualTo(4.0)
        assertThat(Comparison(kotlinNanos = 100, cppNanos = 400, outputsMatch = true).cppSpeedup).isEqualTo(0.25)
        assertThat(Comparison(kotlinNanos = 100, cppNanos = 0, outputsMatch = true).cppSpeedup).isEqualTo(100.0)
    }

    @Test
    fun `a gap under ten percent counts as the same speed`() {
        assertThat(Comparison(kotlinNanos = 105, cppNanos = 100, outputsMatch = true).verdict).isEqualTo(Verdict.ABOUT_THE_SAME)
        assertThat(Comparison(kotlinNanos = 100, cppNanos = 105, outputsMatch = true).verdict).isEqualTo(Verdict.ABOUT_THE_SAME)
        assertThat(Comparison(kotlinNanos = 200, cppNanos = 100, outputsMatch = true).verdict).isEqualTo(Verdict.CPP_FASTER)
        assertThat(Comparison(kotlinNanos = 100, cppNanos = 200, outputsMatch = true).verdict).isEqualTo(Verdict.KOTLIN_FASTER)
    }

    @Test
    fun `matching implementations agree for every cipher`() {
        val benchmark = CipherBenchmark(warmups = 0, runs = 1)

        BenchmarkCipher.entries.forEach { cipher ->
            assertThat(benchmark.compare(cipher, InputSize.SMALL, FakeCiphers.reference(), FakeCiphers.reference()).outputsMatch).isTrue()
        }
    }

    @Test
    fun `different implementations are caught for every cipher`() {
        val benchmark = CipherBenchmark(warmups = 0, runs = 1)

        BenchmarkCipher.entries.forEach { cipher ->
            assertThat(benchmark.compare(cipher, InputSize.SMALL, FakeCiphers.reference(), FakeCiphers.broken()).outputsMatch).isFalse()
        }
    }

    @Test
    fun `warm-up runs are not timed`() {
        var calls = 0
        val counting = FakeCiphers.reference().let { set ->
            set.copy(caesar = object : TextCipher<Int> {
                override fun encrypt(plaintext: String, key: Int): String = set.caesar.encrypt(plaintext, key).also { calls++ }
                override fun decrypt(ciphertext: String, key: Int): String = set.caesar.decrypt(ciphertext, key)
            })
        }

        CipherBenchmark(warmups = 3, runs = 7).compare(BenchmarkCipher.CAESAR, InputSize.SMALL, counting, FakeCiphers.reference())

        assertThat(calls).isEqualTo(10)
    }

    @Test
    fun `call overhead repeats the tiny input`() {
        var calls = 0
        val counting = FakeCiphers.reference().let { set ->
            set.copy(caesar = object : TextCipher<Int> {
                override fun encrypt(plaintext: String, key: Int): String {
                    calls++
                    assertThat(plaintext).hasLength(CipherBenchmark.TINY_INPUT_CHARS)
                    return set.caesar.encrypt(plaintext, key)
                }
                override fun decrypt(ciphertext: String, key: Int): String = ciphertext
            })
        }

        val result = CipherBenchmark(warmups = 0, runs = 1).callOverhead(100, counting, FakeCiphers.reference())

        assertThat(calls).isEqualTo(100)
        assertThat(result.outputsMatch).isTrue()
    }

    @Test
    fun `sample text has the exact requested length`() {
        InputSize.entries.forEach { assertThat(CipherBenchmark.sampleText(it.bytes)).hasLength(it.bytes) }
        assertThat(CipherBenchmark.sampleText(0)).isEmpty()
    }

    private fun scriptedClock(durations: List<Long>): () -> Long {
        val ticks = durations.flatMap { listOf(0L, it) }.iterator()
        return { ticks.next() }
    }
}
