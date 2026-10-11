package com.danyastry.encryption.feature.xor

import com.danyastry.encryption.core.testing.assertRoundTrip
import com.danyastry.encryption.core.testing.hexToBytes
import com.danyastry.encryption.core.testing.toHex
import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Assert.assertThrows
import org.junit.Test

class XorCipherTest {

    private val cipher = XorCipher()
    private val poem = "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal"
    private val poemCiphertext =
        "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a" +
            "653e2b2027630c692b20283165286326302e27282f"

    @Test
    fun `matches cryptopals set 1 challenge 5`() {
        assertThat(cipher.encrypt(poem.encodeToByteArray(), "ICE".encodeToByteArray()).toHex()).isEqualTo(poemCiphertext)
    }

    @Test
    fun `decrypts cryptopals set 1 challenge 5`() {
        assertThat(cipher.decrypt(poemCiphertext.hexToBytes(), "ICE".encodeToByteArray()).decodeToString()).isEqualTo(poem)
    }

    @Test
    fun `xor with zero bytes changes nothing`() {
        val data = "Hello".encodeToByteArray()

        assertThat(cipher.encrypt(data, byteArrayOf(0))).isEqualTo(data)
    }

    @Test
    fun `xor with itself gives zeros`() {
        val data = "secret".encodeToByteArray()

        assertThat(cipher.encrypt(data, data).all { it == 0.toByte() }).isTrue()
    }

    @Test
    fun `recovers the repeating key from a known start`() {
        val recovered = cipher.recoverKeystream(poemCiphertext.hexToBytes(), "Burning 'em,".encodeToByteArray())

        assertThat(recovered.decodeToString()).isEqualTo("ICEICEICEICE")
    }

    @Test
    fun `recovered keystream is as long as the shorter input`() {
        assertThat(cipher.recoverKeystream(ByteArray(3), ByteArray(10))).hasLength(3)
    }

    @Test
    fun `round trips random bytes and keys`() {
        val random = Random(seed = 12)

        repeat(30) {
            assertRoundTrip(cipher, random.nextBytes(random.nextInt(0, 200)), random.nextBytes(random.nextInt(1, 40)))
        }
    }

    @Test
    fun `rejects an empty key`() {
        assertThrows(IllegalArgumentException::class.java) { cipher.encrypt("Hello".encodeToByteArray(), ByteArray(0)) }
    }
}
