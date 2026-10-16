package com.danyastry.encryption.feature.cpp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.AlgorithmInfo
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.cpp.ui.CppRoute

class CppFeature(private val kotlinCiphers: CipherSet) : FeatureEntry {

    override val info = AlgorithmInfo(
        id = "cpp",
        name = "C++ vs Kotlin",
        category = AlgorithmCategory.NATIVE,
        securityStatus = SecurityStatus.BROKEN,
        summary = "Caesar, Vigenère and XOR in C++ through JNI, timed against Kotlin",
    )

    @Composable
    override fun Content(modifier: Modifier) {
        CppRoute(kotlinCiphers = kotlinCiphers, modifier = modifier)
    }
}
