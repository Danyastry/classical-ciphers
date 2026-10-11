package com.danyastry.encryption.feature.substitution

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.substitution.ui.SubstitutionRoute

object SubstitutionFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "simple-substitution",
        name = "Simple substitution",
        category = AlgorithmCategory.SUBSTITUTION,
        securityStatus = SecurityStatus.BROKEN,
        summary = "A shuffled alphabet is the key",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        SubstitutionRoute(modifier = modifier)
    }
}
