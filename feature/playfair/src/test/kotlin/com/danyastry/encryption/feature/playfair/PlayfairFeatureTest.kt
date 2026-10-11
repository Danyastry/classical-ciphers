package com.danyastry.encryption.feature.playfair

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PlayfairFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(PlayfairFeature.info.id).isEqualTo("playfair")
        assertThat(PlayfairFeature.info.category).isEqualTo(AlgorithmCategory.CLASSICAL)
        assertThat(PlayfairFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
