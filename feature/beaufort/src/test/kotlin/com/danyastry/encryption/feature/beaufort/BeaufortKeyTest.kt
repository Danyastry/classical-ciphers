package com.danyastry.encryption.feature.beaufort

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class BeaufortKeyTest {

    @Test
    fun `maps keyword letters to numbers`() {
        assertThat(BeaufortKey("FORT").shifts.toList()).containsExactly(5, 14, 17, 19).inOrder()
    }

    @Test
    fun `rejects empty or non letter keywords`() {
        listOf("", "fort", "FO RT", "FORT1").forEach { keyword ->
            assertThrows(IllegalArgumentException::class.java) { BeaufortKey(keyword) }
        }
    }

    @Test
    fun `normalize uppercases and drops non letters`() {
        assertThat(BeaufortKey.normalize("for-ti 9!")).isEqualTo("FORTI")
        assertThat(BeaufortKey.normalize("123")).isEmpty()
    }
}
