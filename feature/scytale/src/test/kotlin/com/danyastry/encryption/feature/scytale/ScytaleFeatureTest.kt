package com.danyastry.encryption.feature.scytale

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ScytaleFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(ScytaleFeature.info.id).isEqualTo("scytale")
        assertThat(ScytaleFeature.info.category).isEqualTo(AlgorithmCategory.TRANSPOSITION)
        assertThat(ScytaleFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
