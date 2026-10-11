package com.danyastry.encryption.feature.xor

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class XorFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(XorFeature.info.id).isEqualTo("xor")
        assertThat(XorFeature.info.category).isEqualTo(AlgorithmCategory.POLYALPHABETIC)
        assertThat(XorFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
