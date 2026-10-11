package com.danyastry.encryption.feature.affine

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.affine.ui.AffineRoute

object AffineFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "affine",
        name = "Affine cipher",
        category = AlgorithmCategory.SUBSTITUTION,
        securityStatus = SecurityStatus.BROKEN,
        summary = "(a·x + b) mod 26",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        AffineRoute(modifier = modifier)
    }
}
