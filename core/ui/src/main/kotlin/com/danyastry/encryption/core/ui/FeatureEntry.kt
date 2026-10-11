package com.danyastry.encryption.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo

interface FeatureEntry {
    val info: AlgorithmInfo

    @Composable
    fun Content(modifier: Modifier)
}
