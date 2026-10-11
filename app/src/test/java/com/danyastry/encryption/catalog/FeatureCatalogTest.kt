package com.danyastry.encryption.catalog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class FeatureCatalogTest {

    @Test
    fun `empty catalog has no sections`() {
        val catalog = FeatureCatalog(emptyList())

        assertThat(catalog.isEmpty).isTrue()
        assertThat(catalog.sections).isEmpty()
    }

    @Test
    fun `groups features into sections ordered by category`() {
        val aes = feature("aes-gcm", "AES-GCM", AlgorithmCategory.SYMMETRIC)
        val caesar = feature("caesar", "Caesar", AlgorithmCategory.CLASSICAL)
        val sha = feature("sha-256", "SHA-256", AlgorithmCategory.HASH)

        val catalog = FeatureCatalog(listOf(sha, aes, caesar))

        assertThat(catalog.sections.map { it.category }).containsExactly(
            AlgorithmCategory.CLASSICAL,
            AlgorithmCategory.SYMMETRIC,
            AlgorithmCategory.HASH,
        ).inOrder()
    }

    @Test
    fun `sorts features inside a section by name`() {
        val vigenere = feature("vigenere", "Vigenère", AlgorithmCategory.CLASSICAL)
        val atbash = feature("atbash", "Atbash", AlgorithmCategory.CLASSICAL)
        val caesar = feature("caesar", "Caesar", AlgorithmCategory.CLASSICAL)

        val catalog = FeatureCatalog(listOf(vigenere, atbash, caesar))

        assertThat(catalog.sections.single().entries).containsExactly(atbash, caesar, vigenere).inOrder()
    }

    @Test
    fun `finds feature by id`() {
        val caesar = feature("caesar", "Caesar", AlgorithmCategory.CLASSICAL)

        val catalog = FeatureCatalog(listOf(caesar))

        assertThat(catalog.isEmpty).isFalse()
        assertThat(catalog.find("caesar")).isSameInstanceAs(caesar)
        assertThat(catalog.find("unknown")).isNull()
    }

    @Test
    fun `rejects duplicate ids`() {
        val first = feature("caesar", "Caesar", AlgorithmCategory.CLASSICAL)
        val second = feature("caesar", "Caesar shift", AlgorithmCategory.CLASSICAL)

        val error = assertThrows(IllegalArgumentException::class.java) {
            FeatureCatalog(listOf(first, second))
        }
        assertThat(error).hasMessageThat().contains("caesar")
    }

    @Test
    fun `registry has unique ids`() {
        FeatureCatalog(FeatureRegistry.features)
    }

    private fun feature(id: String, name: String, category: AlgorithmCategory): FeatureEntry =
        object : FeatureEntry {
            override val info = AlgorithmInfo(
                id = id,
                name = name,
                category = category,
                securityStatus = SecurityStatus.RECOMMENDED,
                summary = "$name summary",
            )

            @Composable
            override fun Content(modifier: Modifier) = Unit
        }
}
