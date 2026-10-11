package com.danyastry.encryption.feature.rot

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RotFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(RotFeature.info.id).isEqualTo("rot13-rot47")
        assertThat(RotFeature.info.category).isEqualTo(AlgorithmCategory.CLASSICAL)
        assertThat(RotFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
