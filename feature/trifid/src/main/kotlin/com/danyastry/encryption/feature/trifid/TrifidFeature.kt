package com.danyastry.encryption.feature.trifid

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.trifid.ui.TrifidRoute

object TrifidFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "trifid",
        name = "Trifid cipher",
        category = AlgorithmCategory.CLASSICAL,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Bifid in three dimensions: letters become layer, row and column in a 3×3×3 cube",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        TrifidRoute(modifier = modifier)
    }
}
