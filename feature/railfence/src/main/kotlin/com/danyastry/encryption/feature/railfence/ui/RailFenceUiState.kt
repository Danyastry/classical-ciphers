package com.danyastry.encryption.feature.railfence.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.railfence.RailFenceCipher

data class RailFenceUiState(
    val rails: Int = DEFAULT_RAILS,
    val mode: CipherMode = CipherMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
    val zigzag: List<List<Char?>> = emptyList(),
) {
    companion object {
        const val DEFAULT_RAILS = 3
        const val MIN_RAILS = 2
        const val MAX_RAILS = 10
        const val MAX_ZIGZAG_COLUMNS = 40
    }
}

private val cipher = RailFenceCipher()

internal fun RailFenceUiState.recalculated(): RailFenceUiState {
    val output = when (mode) {
        CipherMode.ENCRYPT -> cipher.encrypt(input, rails)
        CipherMode.DECRYPT -> cipher.decrypt(input, rails)
    }
    val plaintext = if (mode == CipherMode.ENCRYPT) input else output
    return copy(output = output, zigzag = zigzag(plaintext.take(RailFenceUiState.MAX_ZIGZAG_COLUMNS), rails))
}

private fun zigzag(text: String, rails: Int): List<List<Char?>> {
    if (text.isEmpty()) return emptyList()
    val pattern = cipher.railPattern(text.length, rails)
    return (0 until rails).map { rail -> text.indices.map { index -> if (pattern[index] == rail) text[index] else null } }
}
