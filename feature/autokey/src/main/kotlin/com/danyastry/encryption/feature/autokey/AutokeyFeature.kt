package com.danyastry.encryption.feature.autokey

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.autokey.ui.AutokeyRoute

object AutokeyFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "autokey",
        name = "Autokey cipher",
        category = AlgorithmCategory.POLYALPHABETIC,
        securityStatus = SecurityStatus.BROKEN,
        summary = "The message extends its own key",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        AutokeyRoute(modifier = modifier)
    }
}
