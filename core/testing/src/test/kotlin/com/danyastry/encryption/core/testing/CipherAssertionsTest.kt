package com.danyastry.encryption.core.testing

import com.danyastry.encryption.core.cryptoapi.ByteCipher
import com.danyastry.encryption.core.cryptoapi.TextCipher
import org.junit.Assert.assertThrows
import org.junit.Test

class CipherAssertionsTest {

    private val reverseText = object : TextCipher<Unit> {
        override fun encrypt(plaintext: String, key: Unit) = plaintext.reversed()
        override fun decrypt(ciphertext: String, key: Unit) = ciphertext.reversed()
    }

    private val brokenText = object : TextCipher<Unit> {
        override fun encrypt(plaintext: String, key: Unit) = plaintext.reversed()
        override fun decrypt(ciphertext: String, key: Unit) = ciphertext
    }

    private val xorBytes = object : ByteCipher<Byte> {
        override fun encrypt(plaintext: ByteArray, key: Byte) = xor(plaintext, key)
        override fun decrypt(ciphertext: ByteArray, key: Byte) = xor(ciphertext, key)
    }

    private val brokenBytes = object : ByteCipher<Byte> {
        override fun encrypt(plaintext: ByteArray, key: Byte) = xor(plaintext, key)
        override fun decrypt(ciphertext: ByteArray, key: Byte) = ciphertext
    }

    @Test
    fun `passes for text cipher that round trips`() {
        assertRoundTrip(reverseText, "hello", Unit)
    }

    @Test
    fun `fails for text cipher that does not round trip`() {
        assertThrows(AssertionError::class.java) { assertRoundTrip(brokenText, "hello", Unit) }
    }

    @Test
    fun `passes for byte cipher that round trips`() {
        assertRoundTrip(xorBytes, "cafebabe".hexToBytes(), 0x5a)
    }

    @Test
    fun `fails for byte cipher that does not round trip`() {
        assertThrows(AssertionError::class.java) {
            assertRoundTrip(brokenBytes, "cafebabe".hexToBytes(), 0x5a)
        }
    }

    private fun xor(input: ByteArray, key: Byte) =
        ByteArray(input.size) { (input[it].toInt() xor key.toInt()).toByte() }
}
