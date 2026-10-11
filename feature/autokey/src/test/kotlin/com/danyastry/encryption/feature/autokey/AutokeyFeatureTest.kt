package com.danyastry.encryption.feature.autokey

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AutokeyFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(AutokeyFeature.info.id).isEqualTo("autokey")
        assertThat(AutokeyFeature.info.category).isEqualTo(AlgorithmCategory.POLYALPHABETIC)
        assertThat(AutokeyFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
