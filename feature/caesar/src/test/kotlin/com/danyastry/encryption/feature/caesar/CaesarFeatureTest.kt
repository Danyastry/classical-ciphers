package com.danyastry.encryption.feature.caesar

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CaesarFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(CaesarFeature.info.id).isEqualTo("caesar")
        assertThat(CaesarFeature.info.category).isEqualTo(AlgorithmCategory.SUBSTITUTION)
        assertThat(CaesarFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
