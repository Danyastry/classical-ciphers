package com.danyastry.encryption.feature.bacon

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class BaconFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(BaconFeature.info.id).isEqualTo("bacon")
        assertThat(BaconFeature.info.category).isEqualTo(AlgorithmCategory.SUBSTITUTION)
        assertThat(BaconFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
