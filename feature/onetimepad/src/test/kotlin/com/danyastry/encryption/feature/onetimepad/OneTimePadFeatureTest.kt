package com.danyastry.encryption.feature.onetimepad

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class OneTimePadFeatureTest {

    @Test
    fun `is listed as a classical cipher with perfect secrecy`() {
        assertThat(OneTimePadFeature.info.id).isEqualTo("one-time-pad")
        assertThat(OneTimePadFeature.info.category).isEqualTo(AlgorithmCategory.POLYALPHABETIC)
        assertThat(OneTimePadFeature.info.securityStatus).isEqualTo(SecurityStatus.PERFECT_SECRECY)
    }
}
