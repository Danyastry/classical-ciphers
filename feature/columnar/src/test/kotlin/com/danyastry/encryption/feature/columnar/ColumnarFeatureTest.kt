package com.danyastry.encryption.feature.columnar

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ColumnarFeatureTest {

    @Test
    fun `is listed as a broken classical cipher`() {
        assertThat(ColumnarFeature.info.id).isEqualTo("columnar-transposition")
        assertThat(ColumnarFeature.info.category).isEqualTo(AlgorithmCategory.CLASSICAL)
        assertThat(ColumnarFeature.info.securityStatus).isEqualTo(SecurityStatus.BROKEN)
    }
}
