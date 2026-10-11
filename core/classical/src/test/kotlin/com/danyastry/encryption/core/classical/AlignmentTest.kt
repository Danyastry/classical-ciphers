package com.danyastry.encryption.core.classical

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AlignmentTest {

    @Test
    fun `pairs letters with their key letter and skips non letters`() {
        val aligned = alignLetters(input = "at, ta", output = "lx, fo", limit = 10) { "LEMON"[it % 5] }

        assertThat(aligned).containsExactly(
            AlignedLetter(input = 'a', key = 'L', output = 'l'),
            AlignedLetter(input = 't', key = 'E', output = 'x'),
            AlignedLetter(input = 't', key = 'M', output = 'f'),
            AlignedLetter(input = 'a', key = 'O', output = 'o'),
        ).inOrder()
    }

    @Test
    fun `stops at the limit`() {
        val aligned = alignLetters(input = "a".repeat(50), output = "b".repeat(50), limit = 8) { 'K' }

        assertThat(aligned).hasSize(8)
    }

    @Test
    fun `empty input gives no letters`() {
        assertThat(alignLetters(input = "", output = "", limit = 8) { 'K' }).isEmpty()
        assertThat(alignLetters(input = "1, 2!", output = "1, 2!", limit = 8) { 'K' }).isEmpty()
    }
}
