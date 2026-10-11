package com.danyastry.encryption.feature.vigenere.ui

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.classical.isAsciiLetter
import com.danyastry.encryption.feature.vigenere.VigenereCipher

enum class VigenereMode { ENCRYPT, DECRYPT }

data class AlignedLetter(val input: Char, val key: Char, val output: Char)

data class VigenereUiState(
    val keyword: String = DEFAULT_KEYWORD,
    val isKeywordValid: Boolean = true,
    val mode: VigenereMode = VigenereMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
    val alignment: List<AlignedLetter> = emptyList(),
) {
    companion object {
        const val DEFAULT_KEYWORD = "LEMON"
        const val MAX_ALIGNED_LETTERS = 32
    }
}

private val cipher = VigenereCipher()

internal fun VigenereUiState.recalculated(): VigenereUiState {
    if (keyword.isEmpty()) return copy(isKeywordValid = false, output = "", alignment = emptyList())

    val key = Keyword(keyword)
    val output = when (mode) {
        VigenereMode.ENCRYPT -> cipher.encrypt(input, key)
        VigenereMode.DECRYPT -> cipher.decrypt(input, key)
    }
    return copy(isKeywordValid = true, output = output, alignment = align(input, output, keyword))
}

private fun align(input: String, output: String, keyword: String): List<AlignedLetter> =
    input.indices
        .filter { input[it].isAsciiLetter() }
        .take(VigenereUiState.MAX_ALIGNED_LETTERS)
        .mapIndexed { letterIndex, position ->
            AlignedLetter(
                input = input[position],
                key = keyword[letterIndex % keyword.length],
                output = output[position],
            )
        }
