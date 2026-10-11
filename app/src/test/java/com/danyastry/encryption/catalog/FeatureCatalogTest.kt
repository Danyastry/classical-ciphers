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
        val railFence = feature("rail-fence", "Rail fence", AlgorithmCategory.TRANSPOSITION)
        val caesar = feature("caesar", "Caesar", AlgorithmCategory.SUBSTITUTION)
        val enigma = feature("enigma", "Enigma", AlgorithmCategory.MACHINE)

        val catalog = FeatureCatalog(listOf(enigma, railFence, caesar))

        assertThat(catalog.sections.map { it.category }).containsExactly(
            AlgorithmCategory.SUBSTITUTION,
            AlgorithmCategory.TRANSPOSITION,
            AlgorithmCategory.MACHINE,
        ).inOrder()
    }

    @Test
    fun `sorts features inside a section by name`() {
        val vigenere = feature("vigenere", "Vigenère", AlgorithmCategory.SUBSTITUTION)
        val atbash = feature("atbash", "Atbash", AlgorithmCategory.SUBSTITUTION)
        val caesar = feature("caesar", "Caesar", AlgorithmCategory.SUBSTITUTION)

        val catalog = FeatureCatalog(listOf(vigenere, atbash, caesar))

        assertThat(catalog.sections.single().entries).containsExactly(atbash, caesar, vigenere).inOrder()
    }

    @Test
    fun `finds feature by id`() {
        val caesar = feature("caesar", "Caesar", AlgorithmCategory.SUBSTITUTION)

        val catalog = FeatureCatalog(listOf(caesar))

        assertThat(catalog.isEmpty).isFalse()
        assertThat(catalog.find("caesar")).isSameInstanceAs(caesar)
        assertThat(catalog.find("unknown")).isNull()
    }

    @Test
    fun `rejects duplicate ids`() {
        val first = feature("caesar", "Caesar", AlgorithmCategory.SUBSTITUTION)
        val second = feature("caesar", "Caesar shift", AlgorithmCategory.SUBSTITUTION)

        val error = assertThrows(IllegalArgumentException::class.java) {
            FeatureCatalog(listOf(first, second))
        }
        assertThat(error).hasMessageThat().contains("caesar")
    }

    @Test
    fun `lists each security status once in enum order`() {
        val broken = feature("caesar", "Caesar", AlgorithmCategory.SUBSTITUTION, SecurityStatus.BROKEN)
        val pad = feature("one-time-pad", "One-time pad", AlgorithmCategory.POLYALPHABETIC, SecurityStatus.PERFECT_SECRECY)
        val alsoBroken = feature("atbash", "Atbash", AlgorithmCategory.SUBSTITUTION, SecurityStatus.BROKEN)

        val catalog = FeatureCatalog(listOf(broken, pad, alsoBroken))

        assertThat(catalog.statuses).containsExactly(SecurityStatus.PERFECT_SECRECY, SecurityStatus.BROKEN).inOrder()
    }

    @Test
    fun `registry groups every cipher into one of the five families`() {
        val catalog = FeatureCatalog(FeatureRegistry.features)

        assertThat(catalog.sections.map { it.category }).containsExactlyElementsIn(AlgorithmCategory.entries).inOrder()
        assertThat(catalog.sections.sumOf { it.entries.size }).isEqualTo(FeatureRegistry.features.size)
    }

    @Test
    fun `registry has unique ids`() {
        FeatureCatalog(FeatureRegistry.features)
    }

    private fun feature(
        id: String,
        name: String,
        category: AlgorithmCategory,
        status: SecurityStatus = SecurityStatus.RECOMMENDED,
    ): FeatureEntry =
        object : FeatureEntry {
            override val info = AlgorithmInfo(
                id = id,
                name = name,
                category = category,
                securityStatus = status,
                summary = "$name summary",
            )

            @Composable
            override fun Content(modifier: Modifier) = Unit
        }
}
