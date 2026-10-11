package com.danyastry.encryption.feature.playfair.ui

import com.danyastry.encryption.core.classical.PolybiusSquare
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.playfair.PlayfairCipher
import com.danyastry.encryption.feature.playfair.PlayfairStep

data class PlayfairUiState(
    val keyword: String = DEFAULT_KEYWORD,
    val square: String = PolybiusSquare.fromKeyword(DEFAULT_KEYWORD).letters,
    val mode: CipherMode = CipherMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
    val steps: List<PlayfairStep> = emptyList(),
    val isOddCiphertext: Boolean = false,
) {
    companion object {
        const val DEFAULT_KEYWORD = "PLAYFAIR EXAMPLE"
        const val MAX_STEPS = 16
    }
}

private val cipher = PlayfairCipher()

internal fun PlayfairUiState.recalculated(): PlayfairUiState {
    val key = PolybiusSquare.fromKeyword(keyword)
    val isOdd = mode == CipherMode.DECRYPT && PlayfairCipher.normalize(input).length % 2 != 0
    if (isOdd) return copy(square = key.letters, output = "", steps = emptyList(), isOddCiphertext = true)

    val steps = when (mode) {
        CipherMode.ENCRYPT -> cipher.encryptSteps(input, key)
        CipherMode.DECRYPT -> cipher.decryptSteps(input, key)
    }
    return copy(
        square = key.letters,
        output = steps.joinToString(separator = if (mode == CipherMode.ENCRYPT) " " else "") { it.output },
        steps = steps.take(PlayfairUiState.MAX_STEPS),
        isOddCiphertext = false,
    )
}
