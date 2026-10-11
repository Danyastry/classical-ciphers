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
        category = AlgorithmCategory.CLASSICAL,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Splits letters into square coordinates, mixes the rows and columns, and joins them back",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        BifidRoute(modifier = modifier)
    }
}
