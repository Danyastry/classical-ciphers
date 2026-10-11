package com.danyastry.encryption.core.testing

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class HexTest {

    @Test
    fun `decodes hex into bytes`() {
        assertThat("00017f80ff".hexToBytes())
            .isEqualTo(byteArrayOf(0x00, 0x01, 0x7f, 0x80.toByte(), 0xff.toByte()))
    }

    @Test
    fun `decodes uppercase hex and ignores whitespace`() {
        assertThat("DE AD\nbe ef".hexToBytes()).isEqualTo("deadbeef".hexToBytes())
    }

    @Test
    fun `decodes empty string into empty array`() {
        assertThat("".hexToBytes()).isEmpty()
    }

    @Test
    fun `encodes bytes as lowercase hex`() {
        assertThat(byteArrayOf(0x00, 0x0a, 0x7f, 0x80.toByte(), 0xff.toByte()).toHex())
            .isEqualTo("000a7f80ff")
    }

    @Test
    fun `round trips every byte value`() {
        val allBytes = ByteArray(256) { it.toByte() }

        assertThat(allBytes.toHex().hexToBytes()).isEqualTo(allBytes)
    }

    @Test
    fun `rejects odd length`() {
        assertThrows(IllegalArgumentException::class.java) { "abc".hexToBytes() }
    }

    @Test
    fun `rejects non hex characters`() {
        assertThrows(IllegalArgumentException::class.java) { "zz".hexToBytes() }
    }
}
