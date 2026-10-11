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
        category = AlgorithmCategory.TABLE_BASED,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Letters as row and column numbers",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        PolybiusRoute(modifier = modifier)
    }
}
