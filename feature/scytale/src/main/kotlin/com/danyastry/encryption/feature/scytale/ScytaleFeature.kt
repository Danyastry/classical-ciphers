package com.danyastry.encryption.feature.scytale

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.scytale.ui.ScytaleRoute

object ScytaleFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "scytale",
        name = "Scytale",
        category = AlgorithmCategory.CLASSICAL,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Spartan strip wound around a rod: write along the rod, unwind to read",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        ScytaleRoute(modifier = modifier)
    }
}
