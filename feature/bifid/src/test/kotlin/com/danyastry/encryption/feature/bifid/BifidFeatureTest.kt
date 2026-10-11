package com.danyastry.encryption.feature.bifid

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class BifidFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(BifidFeature.info.id).isEqualTo("bifid")
        assertThat(BifidFeature.info.category).isEqualTo(AlgorithmCategory.TABLE_BASED)
        assertThat(BifidFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
