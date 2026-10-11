package com.danyastry.encryption.core.ui

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AlgorithmLabelsTest {

    @Test
    fun `every category has its own title`() {
        val titles = AlgorithmCategory.entries.map { it.titleRes }

        assertThat(titles).containsNoDuplicates()
    }

    @Test
    fun `every category has its own description`() {
        assertThat(AlgorithmCategory.entries.map { it.descriptionRes }).containsNoDuplicates()
    }

    @Test
    fun `every security status has its own description`() {
        assertThat(SecurityStatus.entries.map { it.descriptionRes }).containsNoDuplicates()
    }

    @Test
    fun `every security status has its own label`() {
        val labels = SecurityStatus.entries.map { it.labelRes }

        assertThat(labels).containsNoDuplicates()
    }
}
