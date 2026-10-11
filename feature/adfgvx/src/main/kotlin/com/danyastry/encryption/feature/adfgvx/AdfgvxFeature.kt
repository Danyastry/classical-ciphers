package com.danyastry.encryption.feature.adfgvx

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.adfgvx.ui.AdfgvxRoute

object AdfgvxFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "adfgvx",
        name = "ADFGVX cipher",
        category = AlgorithmCategory.TABLE_BASED,
        securityStatus = SecurityStatus.BROKEN,
        summary = "6×6 square plus transposition",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        AdfgvxRoute(modifier = modifier)
    }
}
