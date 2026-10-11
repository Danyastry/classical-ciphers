package com.danyastry.encryption.feature.scytale.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.scytale.ScytaleCipher

data class ScytaleUiState(
    val turns: Int = DEFAULT_TURNS,
    val mode: CipherMode = CipherMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
    val wrapped: List<String> = emptyList(),
) {
    companion object {
        const val DEFAULT_TURNS = 4
        const val MIN_TURNS = 2
        const val MAX_TURNS = 10
        const val MAX_WRAPPED_COLUMNS = 40
    }
}

private val cipher = ScytaleCipher()

internal fun ScytaleUiState.recalculated(): ScytaleUiState {
    val output = when (mode) {
        CipherMode.ENCRYPT -> cipher.encrypt(input, turns)
        CipherMode.DECRYPT -> cipher.decrypt(input, turns)
    }
    val plaintext = if (mode == CipherMode.ENCRYPT) input else output
    return copy(
        output = output,
        wrapped = cipher.wrap(plaintext, turns).map { it.take(ScytaleUiState.MAX_WRAPPED_COLUMNS) },
    )
}
