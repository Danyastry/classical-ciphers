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
        category = AlgorithmCategory.CLASSICAL,
        securityStatus = SecurityStatus.BROKEN,
        summary = "WWI German field cipher: a 6×6 square followed by a columnar transposition",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        AdfgvxRoute(modifier = modifier)
    }
}
