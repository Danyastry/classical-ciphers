package com.danyastry.encryption.feature.railfence

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RailFenceFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(RailFenceFeature.info.id).isEqualTo("rail-fence")
        assertThat(RailFenceFeature.info.category).isEqualTo(AlgorithmCategory.CLASSICAL)
        assertThat(RailFenceFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
