package com.danyastry.encryption.feature.atbash

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.atbash.ui.AtbashRoute

object AtbashFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "atbash",
        name = "Atbash cipher",
        category = AlgorithmCategory.SUBSTITUTION,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Reversed alphabet: A=Z, B=Y",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        AtbashRoute(modifier = modifier)
    }
}
