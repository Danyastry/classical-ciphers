package com.danyastry.encryption.feature.cpp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
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
    fun keepsACharacterOutsideTheBasicPlaneInOnePiece() {
        val text = "Hi \uD835\uDD38 there"

        val encrypted = NativeCaesarCipher().encrypt(text, 3)

        assertThat(encrypted).isEqualTo("Kl \uD835\uDD38 wkhuh")
        assertThat(encrypted).hasLength(text.length)
    }

    @Test
    fun doesNotChangeTheInputArrays() {
        val data = "Attack at dawn".encodeToByteArray()
        val key = "KEY".encodeToByteArray()
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
