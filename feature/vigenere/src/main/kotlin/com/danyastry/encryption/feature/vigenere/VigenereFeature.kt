package com.danyastry.encryption.feature.vigenere

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.vigenere.ui.VigenereRoute

object VigenereFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "vigenere",
        name = "Vigenère cipher",
        category = AlgorithmCategory.POLYALPHABETIC,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Caesar shifts taken from a keyword",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        VigenereRoute(modifier = modifier)
    }
}
