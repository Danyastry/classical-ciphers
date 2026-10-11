package com.danyastry.encryption.feature.hill

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class HillFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(HillFeature.info.id).isEqualTo("hill")
        assertThat(HillFeature.info.category).isEqualTo(AlgorithmCategory.TABLE_BASED)
        assertThat(HillFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
