package com.danyastry.encryption.feature.rot

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RotCipherTest {

    private val rot13 = RotCipher(RotVariant.ROT13)
    private val rot47 = RotCipher(RotVariant.ROT47)

    private val printableAscii = ('!'..'~').joinToString("")

    @Test
    fun `rot13 rotates letters by thirteen`() {
        assertThat(rot13.transform("Hello, World!")).isEqualTo("Uryyb, Jbeyq!")
    }

    @Test
    fun `rot13 maps the alphabet onto its second half`() {
        assertThat(rot13.transform("abcdefghijklmnopqrstuvwxyz")).isEqualTo("nopqrstuvwxyzabcdefghijklm")
        assertThat(rot13.transform("ABCDEFGHIJKLMNOPQRSTUVWXYZ")).isEqualTo("NOPQRSTUVWXYZABCDEFGHIJKLM")
    }

    @Test
    fun `rot13 leaves digits and symbols untouched`() {
        assertThat(rot13.transform("0123456789 !@#\$%^&*()")).isEqualTo("0123456789 !@#\$%^&*()")
    }

    @Test
    fun `rot47 matches the reference example`() {
        assertThat(rot47.transform("The Quick Brown Fox Jumps Over The Lazy Dog."))
            .isEqualTo("%96 \"F:4< qC@H? u@I yF>AD ~G6C %96 {2KJ s@8]")
    }

    @Test
    fun `rot47 rotates digits and symbols too`() {
        assertThat(rot47.transform("Hello, World! 123")).isEqualTo("w6==@[ (@C=5P `ab")
    }

    @Test
    fun `rot47 keeps the printable ascii range closed`() {
        val rotated = rot47.transform(printableAscii)

        assertThat(rotated.toSet()).isEqualTo(printableAscii.toSet())
        assertThat(rotated.first()).isEqualTo('P')
    }

    @Test
    fun `rot47 leaves spaces and control characters untouched`() {
        assertThat(rot47.transform(" \t\n")).isEqualTo(" \t\n")
    }

    @Test
    fun `both variants are their own inverse`() {
        val text = "Why did the chicken cross the road? To get to the other side! 42 ~"

        listOf(rot13, rot47).forEach { cipher ->
            assertThat(cipher.transform(cipher.transform(text))).isEqualTo(text)
        }
        assertThat(rot47.transform(rot47.transform(printableAscii))).isEqualTo(printableAscii)
    }

    @Test
    fun `handles empty text`() {
        assertThat(rot13.transform("")).isEmpty()
        assertThat(rot47.transform("")).isEmpty()
    }
}
