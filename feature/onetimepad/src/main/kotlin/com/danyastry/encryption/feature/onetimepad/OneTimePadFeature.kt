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
        category = AlgorithmCategory.CLASSICAL,
        securityStatus = SecurityStatus.PERFECT_SECRECY,
        summary = "Unbreakable when the key is truly random, as long as the message and never reused",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        OneTimePadRoute(modifier = modifier)
    }
}
