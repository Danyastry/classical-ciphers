package com.danyastry.encryption.feature.caesar.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.caesar.BruteForceCandidate
import com.danyastry.encryption.feature.caesar.CaesarCipher

data class CaesarUiState(
    val shift: Int = 3,
    val mode: CipherMode = CipherMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
    val candidates: List<BruteForceCandidate> = emptyList(),
)

private val cipher = CaesarCipher()

internal fun CaesarUiState.recalculated(): CaesarUiState = copy(
    output = when (mode) {
        CipherMode.ENCRYPT -> cipher.encrypt(input, shift)
        CipherMode.DECRYPT -> cipher.decrypt(input, shift)
    },
    candidates = if (mode == CipherMode.DECRYPT && input.isNotEmpty()) cipher.bruteForce(input) else emptyList(),
)
