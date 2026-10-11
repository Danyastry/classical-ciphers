package com.danyastry.encryption.feature.vigenere

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class VigenereFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(VigenereFeature.info.id).isEqualTo("vigenere")
        assertThat(VigenereFeature.info.category).isEqualTo(AlgorithmCategory.POLYALPHABETIC)
        assertThat(VigenereFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
