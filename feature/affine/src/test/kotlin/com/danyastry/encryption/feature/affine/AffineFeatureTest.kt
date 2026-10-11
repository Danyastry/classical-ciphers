package com.danyastry.encryption.feature.affine

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AffineFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(AffineFeature.info.id).isEqualTo("affine")
        assertThat(AffineFeature.info.category).isEqualTo(AlgorithmCategory.CLASSICAL)
        assertThat(AffineFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
