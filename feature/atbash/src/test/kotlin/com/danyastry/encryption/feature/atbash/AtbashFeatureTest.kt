package com.danyastry.encryption.feature.atbash

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AtbashFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(AtbashFeature.info.id).isEqualTo("atbash")
        assertThat(AtbashFeature.info.category).isEqualTo(AlgorithmCategory.SUBSTITUTION)
        assertThat(AtbashFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
