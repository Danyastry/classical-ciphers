package com.danyastry.encryption.feature.bifid

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.bifid.ui.BifidRoute

object BifidFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "bifid",
        name = "Bifid cipher",
        category = AlgorithmCategory.TABLE_BASED,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Mixes row and column numbers",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        BifidRoute(modifier = modifier)
    }
}
