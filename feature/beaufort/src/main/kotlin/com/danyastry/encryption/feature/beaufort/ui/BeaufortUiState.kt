package com.danyastry.encryption.feature.beaufort.ui

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.classical.isAsciiLetter
import com.danyastry.encryption.feature.beaufort.BeaufortCipher

data class AlignedLetter(val input: Char, val key: Char, val output: Char)

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
    return copy(isKeywordValid = true, output = output, alignment = align(input, output, keyword))
}

private fun align(input: String, output: String, keyword: String): List<AlignedLetter> =
    input.indices
        .filter { input[it].isAsciiLetter() }
        .take(BeaufortUiState.MAX_ALIGNED_LETTERS)
        .mapIndexed { letterIndex, position ->
            AlignedLetter(
                input = input[position],
                key = keyword[letterIndex % keyword.length],
                output = output[position],
            )
        }
