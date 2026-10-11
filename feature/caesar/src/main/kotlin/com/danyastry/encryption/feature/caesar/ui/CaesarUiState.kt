package com.danyastry.encryption.feature.caesar.ui

import com.danyastry.encryption.feature.caesar.BruteForceCandidate
import com.danyastry.encryption.feature.caesar.CaesarCipher

enum class CaesarMode { ENCRYPT, DECRYPT }

data class CaesarUiState(
    val shift: Int = 3,
    val mode: CaesarMode = CaesarMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
    val candidates: List<BruteForceCandidate> = emptyList(),
)

private val cipher = CaesarCipher()

internal fun CaesarUiState.recalculated(): CaesarUiState = copy(
    output = when (mode) {
        CaesarMode.ENCRYPT -> cipher.encrypt(input, shift)
        CaesarMode.DECRYPT -> cipher.decrypt(input, shift)
    },
    candidates = if (mode == CaesarMode.DECRYPT && input.isNotEmpty()) cipher.bruteForce(input) else emptyList(),
)
