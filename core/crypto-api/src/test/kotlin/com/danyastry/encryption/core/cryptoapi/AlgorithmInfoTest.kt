package com.danyastry.encryption.core.cryptoapi

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class AlgorithmInfoTest {

    @Test
    fun `accepts kebab-case id`() {
        val info = algorithmInfo(id = "aes-256-gcm")

        assertThat(info.id).isEqualTo("aes-256-gcm")
    }

    @Test
    fun `accepts single word id`() {
        val info = algorithmInfo(id = "caesar")

        assertThat(info.id).isEqualTo("caesar")
    }

    @Test
    fun `rejects ids that are not lowercase kebab-case`() {
        listOf("", "AES", "aes_gcm", "aes gcm", "-aes", "aes-", "aes--gcm", "aes/gcm").forEach { id ->
            assertThrows(IllegalArgumentException::class.java) { algorithmInfo(id = id) }
        }
    }

    @Test
    fun `rejects blank name`() {
        assertThrows(IllegalArgumentException::class.java) { algorithmInfo(name = "  ") }
    }

    private fun algorithmInfo(id: String = "caesar", name: String = "Caesar") = AlgorithmInfo(
        id = id,
        name = name,
        category = AlgorithmCategory.CLASSICAL,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Shift cipher",
    )
}
