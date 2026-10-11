package com.danyastry.encryption.feature.bacon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.bacon.ui.BaconRoute

object BaconFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "bacon",
        name = "Bacon's cipher",
        category = AlgorithmCategory.SUBSTITUTION,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Each letter as five A/B symbols",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        BaconRoute(modifier = modifier)
    }
}
