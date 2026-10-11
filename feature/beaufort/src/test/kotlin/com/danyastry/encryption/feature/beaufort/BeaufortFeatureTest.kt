package com.danyastry.encryption.feature.beaufort

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class BeaufortFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(BeaufortFeature.info.id).isEqualTo("beaufort")
        assertThat(BeaufortFeature.info.category).isEqualTo(AlgorithmCategory.CLASSICAL)
        assertThat(BeaufortFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
