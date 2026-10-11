package com.danyastry.encryption.feature.hill

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.hill.ui.HillRoute

object HillFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "hill",
        name = "Hill cipher",
        category = AlgorithmCategory.TABLE_BASED,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Letter blocks times a key matrix",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        HillRoute(modifier = modifier)
    }
}
