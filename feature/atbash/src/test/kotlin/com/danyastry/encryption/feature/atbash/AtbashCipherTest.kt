package com.danyastry.encryption.feature.atbash

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AtbashCipherTest {

    private val cipher = AtbashCipher()

    @Test
    fun `mirrors the alphabet`() {
        assertThat(cipher.transform("abcdefghijklmnopqrstuvwxyz")).isEqualTo("zyxwvutsrqponmlkjihgfedcba")
        assertThat(cipher.transform("ABCDEFGHIJKLMNOPQRSTUVWXYZ")).isEqualTo("ZYXWVUTSRQPONMLKJIHGFEDCBA")
    }

    @Test
    fun `matches the reference example`() {
        assertThat(cipher.transform("WIZARD")).isEqualTo("DRAZIW")
    }

    @Test
    fun `preserves case and leaves non letters untouched`() {
        assertThat(cipher.transform("Hello, World! 123")).isEqualTo("Svool, Dliow! 123")
    }

    @Test
    fun `no letter maps to itself`() {
        ('a'..'z').forEach { letter ->
            assertThat(cipher.transform(letter.toString())).isNotEqualTo(letter.toString())
        }
    }

    @Test
    fun `is its own inverse`() {
        val text = "The Quick Brown Fox Jumps Over The Lazy Dog, 42 times!"

        assertThat(cipher.transform(cipher.transform(text))).isEqualTo(text)
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.transform("")).isEmpty()
    }
}
