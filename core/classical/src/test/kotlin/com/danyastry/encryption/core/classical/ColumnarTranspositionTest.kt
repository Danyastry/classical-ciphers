package com.danyastry.encryption.core.classical

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ColumnarTranspositionTest {

    private val zebras = Keyword("ZEBRAS")

    @Test
    fun `orders columns alphabetically and keeps ties left to right`() {
        assertThat(ColumnarTransposition.columnOrder(zebras)).containsExactly(4, 2, 1, 3, 5, 0).inOrder()
        assertThat(ColumnarTransposition.columnOrder(Keyword("APPLE"))).containsExactly(0, 4, 3, 1, 2).inOrder()
    }

    @Test
    fun `matches the wikipedia example`() {
        assertThat(ColumnarTransposition.encrypt("WEAREDISCOVEREDFLEEATONCE", zebras))
            .isEqualTo("EVLNACDTESEAROFODEECWIREE")
        assertThat(ColumnarTransposition.decrypt("EVLNACDTESEAROFODEECWIREE", zebras))
            .isEqualTo("WEAREDISCOVEREDFLEEATONCE")
    }

    @Test
    fun `round trips every length`() {
        listOf("A", "KEY", "ZEBRAS", "APPLE", "PRIVACY").forEach { keyword ->
            (0..30).forEach { length ->
                val text = "abcdefghijklmnopqrstuvwxyz0123".take(length)
                val key = Keyword(keyword)
                assertThat(ColumnarTransposition.decrypt(ColumnarTransposition.encrypt(text, key), key)).isEqualTo(text)
            }
        }
    }
}
