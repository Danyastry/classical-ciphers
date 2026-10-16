package com.danyastry.encryption.feature.cpp.ui

import com.danyastry.encryption.feature.cpp.BenchmarkCipher
import com.danyastry.encryption.feature.cpp.Comparison
import com.danyastry.encryption.feature.cpp.InputSize

data class CppUiState(
    val cipher: BenchmarkCipher = BenchmarkCipher.CAESAR,
    val size: InputSize = InputSize.MEDIUM,
    val isRunning: Boolean = false,
    val comparison: Comparison? = null,
    val overhead: Comparison? = null,
    val abi: String = "",
)
