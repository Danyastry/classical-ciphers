package com.danyastry.encryption.feature.cpp

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CppFeatureTest {

    @Test
    fun `is listed under native code`() {
        val info = CppFeature(FakeCiphers.reference()).info

        assertThat(info.id).isEqualTo("cpp")
        assertThat(info.category).isEqualTo(AlgorithmCategory.NATIVE)
        assertThat(info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
