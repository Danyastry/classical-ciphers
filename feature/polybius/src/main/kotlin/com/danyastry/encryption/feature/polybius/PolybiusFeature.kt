package com.danyastry.encryption.feature.polybius

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.polybius.ui.PolybiusRoute

object PolybiusFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "polybius-square",
        name = "Polybius square",
        category = AlgorithmCategory.CLASSICAL,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Replaces every letter with its row and column number in a 5×5 square",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        PolybiusRoute(modifier = modifier)
    }
}
