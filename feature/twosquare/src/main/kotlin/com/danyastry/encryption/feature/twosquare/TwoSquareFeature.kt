package com.danyastry.encryption.feature.twosquare

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.twosquare.ui.TwoSquareRoute

object TwoSquareFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "two-square",
        name = "Two-square cipher",
        category = AlgorithmCategory.TABLE_BASED,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Letter pairs in two squares",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        TwoSquareRoute(modifier = modifier)
    }
}
