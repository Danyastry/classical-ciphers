package com.danyastry.encryption.feature.columnar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.columnar.ui.ColumnarRoute

object ColumnarFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "columnar-transposition",
        name = "Columnar transposition",
        category = AlgorithmCategory.TRANSPOSITION,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Columns read in keyword order",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        ColumnarRoute(modifier = modifier)
    }
}
