package com.danyastry.encryption.feature.playfair

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.playfair.ui.PlayfairRoute

object PlayfairFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "playfair",
        name = "Playfair cipher",
        category = AlgorithmCategory.CLASSICAL,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Encrypts pairs of letters using a 5×5 square built from a keyword",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        PlayfairRoute(modifier = modifier)
    }
}
