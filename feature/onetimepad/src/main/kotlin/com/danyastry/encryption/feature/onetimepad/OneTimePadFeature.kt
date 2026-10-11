package com.danyastry.encryption.feature.onetimepad

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.onetimepad.ui.OneTimePadRoute

object OneTimePadFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "one-time-pad",
        name = "One-time pad",
        category = AlgorithmCategory.POLYALPHABETIC,
        securityStatus = SecurityStatus.PERFECT_SECRECY,
        summary = "Random key as long as the text",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        OneTimePadRoute(modifier = modifier)
    }
}
