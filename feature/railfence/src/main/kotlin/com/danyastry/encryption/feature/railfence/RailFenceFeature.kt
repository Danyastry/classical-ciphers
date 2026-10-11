package com.danyastry.encryption.feature.railfence

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.railfence.ui.RailFenceRoute

object RailFenceFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "rail-fence",
        name = "Rail fence cipher",
        category = AlgorithmCategory.CLASSICAL,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Writes the text in a zigzag across several rails and reads it row by row",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        RailFenceRoute(modifier = modifier)
    }
}
