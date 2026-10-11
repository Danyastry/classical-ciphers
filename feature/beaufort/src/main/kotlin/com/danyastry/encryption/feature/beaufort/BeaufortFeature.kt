package com.danyastry.encryption.feature.beaufort

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.beaufort.ui.BeaufortRoute

object BeaufortFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "beaufort",
        name = "Beaufort cipher",
        category = AlgorithmCategory.CLASSICAL,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Subtracts each letter from a repeating keyword, so encrypting twice gives the plaintext back",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        BeaufortRoute(modifier = modifier)
    }
}
