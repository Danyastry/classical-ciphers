package com.danyastry.encryption.feature.beaufort.ui

import com.danyastry.encryption.core.classical.AlignedLetter
import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.classical.alignLetters
import com.danyastry.encryption.feature.beaufort.BeaufortCipher

data class BeaufortUiState(
    val keyword: String = DEFAULT_KEYWORD,
    val isKeywordValid: Boolean = true,
    val input: String = "",
    val output: String = "",
    val alignment: List<AlignedLetter> = emptyList(),
) {
    companion object {
        const val DEFAULT_KEYWORD = "FORTIFICATION"
        const val MAX_ALIGNED_LETTERS = 32
    }
}

private val cipher = BeaufortCipher()

internal fun BeaufortUiState.recalculated(): BeaufortUiState {
    if (keyword.isEmpty()) return copy(isKeywordValid = false, output = "", alignment = emptyList())

    val output = cipher.encrypt(input, Keyword(keyword))
    return copy(
        isKeywordValid = true,
        output = output,
        alignment = alignLetters(input, output, BeaufortUiState.MAX_ALIGNED_LETTERS) { keyword[it % keyword.length] },
    )
}
