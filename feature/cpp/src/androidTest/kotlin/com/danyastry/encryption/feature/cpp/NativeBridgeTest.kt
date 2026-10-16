package com.danyastry.encryption.feature.cpp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.danyastry.encryption.core.classical.Keyword
import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NativeBridgeTest {

    @Test
    fun handlesOneMegabyteOfText() {
        val text = CipherBenchmark.sampleText(InputSize.LARGE.bytes)
        val cipher = NativeCaesarCipher()

        val encrypted = cipher.encrypt(text, 3)

        assertThat(encrypted).hasLength(text.length)
        assertThat(cipher.decrypt(encrypted, 3)).isEqualTo(text)
    }

    @Test
    fun keepsSurrogatePairsIntact() {
        val text = "a😀b𝄞c"

        assertThat(NativeVigenereCipher().encrypt(text, Keyword("B"))).isEqualTo("b😀c𝄞d")
    }

    @Test
    fun doesNotChangeTheInputArrays() {
        val data = Random(1).nextBytes(4_096)
        val key = Random(2).nextBytes(17)
        val dataCopy = data.copyOf()
        val keyCopy = key.copyOf()

        NativeXorCipher().encrypt(data, key)

        assertThat(data).isEqualTo(dataCopy)
        assertThat(key).isEqualTo(keyCopy)
    }

    @Test
    fun returnsANewArray() {
        val data = ByteArray(8)

        val result = NativeXorCipher().encrypt(data, byteArrayOf(0))

        assertThat(result).isNotSameInstanceAs(data)
        assertThat(result).isEqualTo(data)
    }

    @Test
    fun benchmarkAgreesForEveryCipherAndSize() {
        val benchmark = CipherBenchmark(warmups = 0, runs = 1)

        BenchmarkCipher.entries.forEach { cipher ->
            InputSize.entries.forEach { size ->
                assertThat(benchmark.compare(cipher, size, CipherSet.Native, CipherSet.Native).outputsMatch).isTrue()
            }
        }
    }
}
