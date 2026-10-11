package com.danyastry.encryption.feature.vigenere

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class VigenereKeyTest {

    @Test
    fun `maps keyword letters to shifts`() {
        assertThat(VigenereKey("LEMON").shifts.toList()).containsExactly(11, 4, 12, 14, 13).inOrder()
        assertThat(VigenereKey("AZ").shifts.toList()).containsExactly(0, 25).inOrder()
    }

    @Test
    fun `rejects empty or non letter keywords`() {
        listOf("", "lemon", "LEM ON", "LEMON1").forEach { keyword ->
            assertThrows(IllegalArgumentException::class.java) { VigenereKey(keyword) }
        }
    }

    @Test
    fun `normalize uppercases and drops non letters`() {
        assertThat(VigenereKey.normalize("le-mon 42!")).isEqualTo("LEMON")
        assertThat(VigenereKey.normalize("123")).isEmpty()
    }
}
