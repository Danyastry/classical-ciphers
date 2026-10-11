package com.danyastry.encryption.feature.autokey

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class AutokeyKeyTest {

    @Test
    fun `accepts uppercase primers`() {
        assertThat(AutokeyKey("QUEENLY").primer).isEqualTo("QUEENLY")
    }

    @Test
    fun `rejects empty or non letter primers`() {
        listOf("", "queenly", "QUEEN LY", "QUEENLY1").forEach { primer ->
            assertThrows(IllegalArgumentException::class.java) { AutokeyKey(primer) }
        }
    }

    @Test
    fun `normalize uppercases and drops non letters`() {
        assertThat(AutokeyKey.normalize("queen-ly 7!")).isEqualTo("QUEENLY")
        assertThat(AutokeyKey.normalize("123")).isEmpty()
    }
}
