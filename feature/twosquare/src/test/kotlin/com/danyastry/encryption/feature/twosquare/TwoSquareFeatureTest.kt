package com.danyastry.encryption.feature.twosquare

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TwoSquareFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(TwoSquareFeature.info.id).isEqualTo("two-square")
        assertThat(TwoSquareFeature.info.category).isEqualTo(AlgorithmCategory.CLASSICAL)
        assertThat(TwoSquareFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
