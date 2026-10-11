package com.danyastry.encryption.feature.polybius

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PolybiusFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(PolybiusFeature.info.id).isEqualTo("polybius-square")
        assertThat(PolybiusFeature.info.category).isEqualTo(AlgorithmCategory.CLASSICAL)
        assertThat(PolybiusFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
