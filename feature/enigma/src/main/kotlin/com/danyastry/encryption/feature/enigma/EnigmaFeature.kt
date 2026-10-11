package com.danyastry.encryption.feature.enigma

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.enigma.ui.EnigmaRoute

object EnigmaFeature : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "enigma",
        name = "Enigma I",
        category = AlgorithmCategory.CLASSICAL,
        securityStatus = SecurityStatus.BROKEN,
        summary = "WWII German rotor machine with rings, plugboard and the double-stepping middle rotor",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        EnigmaRoute(modifier = modifier)
    }
}
