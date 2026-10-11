package com.danyastry.encryption.feature.foursquare

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FourSquareFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(FourSquareFeature.info.id).isEqualTo("four-square")
        assertThat(FourSquareFeature.info.category).isEqualTo(AlgorithmCategory.TABLE_BASED)
        assertThat(FourSquareFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
