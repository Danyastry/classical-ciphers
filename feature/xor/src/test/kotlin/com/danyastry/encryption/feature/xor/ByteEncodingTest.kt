package com.danyastry.encryption.feature.xor

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ByteEncodingTest {

    private val bytes = byteArrayOf(0x23, 0x00, 0x15, 0x07, 0x0a)

    @Test
    fun `encodes hex in lowercase pairs`() {
        assertThat(ByteEncoding.HEX.encode(bytes)).isEqualTo("230015070a")
        assertThat(ByteEncoding.HEX.encode(byteArrayOf(-1))).isEqualTo("ff")
    }

    @Test
    fun `encodes base64`() {
        assertThat(ByteEncoding.BASE64.encode(bytes)).isEqualTo("IwAVBwo=")
    }

    @Test
    fun `decodes both encodings ignoring whitespace`() {
        assertThat(ByteEncoding.HEX.decodeOrNull("23 00 15\n07 0A")).isEqualTo(bytes)
        assertThat(ByteEncoding.BASE64.decodeOrNull(" IwAV Bwo= ")).isEqualTo(bytes)
    }

    @Test
    fun `invalid input decodes to null`() {
        assertThat(ByteEncoding.HEX.decodeOrNull("abc")).isNull()
        assertThat(ByteEncoding.HEX.decodeOrNull("zz")).isNull()
        assertThat(ByteEncoding.BASE64.decodeOrNull("***")).isNull()
    }

    @Test
    fun `empty input is an empty array`() {
        assertThat(ByteEncoding.HEX.decodeOrNull("")).isEmpty()
        assertThat(ByteEncoding.BASE64.decodeOrNull("")).isEmpty()
    }

    @Test
    fun `round trips every byte value`() {
        val all = ByteArray(256) { it.toByte() }

        ByteEncoding.entries.forEach { encoding ->
            assertThat(encoding.decodeOrNull(encoding.encode(all))).isEqualTo(all)
        }
    }
}
