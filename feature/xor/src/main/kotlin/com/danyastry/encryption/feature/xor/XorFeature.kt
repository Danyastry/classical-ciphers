package com.danyastry.encryption.feature.xor

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.xor.ui.XorRoute

object XorFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "xor",
        name = "XOR cipher",
        category = AlgorithmCategory.POLYALPHABETIC,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Bytes XORed with a repeating key",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        XorRoute(modifier = modifier)
    }
}
