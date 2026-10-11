package com.danyastry.encryption.core.classical

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class KeywordTest {

    @Test
    fun `shifts follow the letters and wrap around`() {
        val lemon = Keyword("LEMON")

        assertThat((0 until 7).map { lemon.shiftAt(it) }).containsExactly(11, 4, 12, 14, 13, 11, 4).inOrder()
        assertThat(lemon.length).isEqualTo(5)
    }

    @Test
    fun `A is zero and Z is twenty five`() {
        val key = Keyword("AZ")

        assertThat(key.shiftAt(0)).isEqualTo(0)
        assertThat(key.shiftAt(1)).isEqualTo(25)
    }

    @Test
    fun `rejects empty or non letter keywords`() {
        listOf("", "lemon", "LEM ON", "LEMON1").forEach { letters ->
            assertThrows(IllegalArgumentException::class.java) { Keyword(letters) }
        }
    }

    @Test
    fun `normalize uppercases and drops non letters`() {
        assertThat(Keyword.normalize("le-mon 42!")).isEqualTo("LEMON")
        assertThat(Keyword.normalize("123")).isEmpty()
    }
}
