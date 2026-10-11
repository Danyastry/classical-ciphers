package com.danyastry.encryption.feature.caesar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.caesar.ui.CaesarRoute

object CaesarFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "caesar",
        name = "Caesar cipher",
        category = AlgorithmCategory.CLASSICAL,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Shifts every letter by a fixed number of positions",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        CaesarRoute(modifier = modifier)
    }
}
