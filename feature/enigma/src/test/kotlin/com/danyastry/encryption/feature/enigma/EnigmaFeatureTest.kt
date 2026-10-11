package com.danyastry.encryption.feature.enigma

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EnigmaFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(EnigmaFeature.info.id).isEqualTo("enigma")
        assertThat(EnigmaFeature.info.category).isEqualTo(AlgorithmCategory.MACHINE)
        assertThat(EnigmaFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
