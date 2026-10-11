package com.danyastry.encryption.feature.rot

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.rot.ui.RotRoute

object RotFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "rot13-rot47",
        name = "ROT13 / ROT47",
        category = AlgorithmCategory.CLASSICAL,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Keyless rotations that undo themselves when applied twice",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        RotRoute(modifier = modifier)
    }
}
