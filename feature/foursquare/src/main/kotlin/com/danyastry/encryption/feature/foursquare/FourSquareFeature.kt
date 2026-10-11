package com.danyastry.encryption.feature.foursquare

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.foursquare.ui.FourSquareRoute

object FourSquareFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "four-square",
        name = "Four-square cipher",
        category = AlgorithmCategory.TABLE_BASED,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Letter pairs in four squares",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        FourSquareRoute(modifier = modifier)
    }
}
