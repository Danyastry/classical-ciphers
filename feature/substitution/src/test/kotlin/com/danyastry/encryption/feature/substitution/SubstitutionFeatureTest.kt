package com.danyastry.encryption.feature.substitution

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SubstitutionFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(SubstitutionFeature.info.id).isEqualTo("simple-substitution")
        assertThat(SubstitutionFeature.info.category).isEqualTo(AlgorithmCategory.SUBSTITUTION)
        assertThat(SubstitutionFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
