package com.danyastry.encryption.feature.adfgvx

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AdfgvxFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(AdfgvxFeature.info.id).isEqualTo("adfgvx")
        assertThat(AdfgvxFeature.info.category).isEqualTo(AlgorithmCategory.TABLE_BASED)
        assertThat(AdfgvxFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
