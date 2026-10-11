package com.danyastry.encryption.feature.trifid

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TrifidFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(TrifidFeature.info.id).isEqualTo("trifid")
        assertThat(TrifidFeature.info.category).isEqualTo(AlgorithmCategory.CLASSICAL)
        assertThat(TrifidFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
