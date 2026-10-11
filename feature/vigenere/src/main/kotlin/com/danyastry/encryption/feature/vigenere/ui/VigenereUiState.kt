package com.danyastry.encryption.feature.vigenere.ui

import com.danyastry.encryption.core.classical.AlignedLetter
import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.classical.alignLetters
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.vigenere.VigenereCipher

data class VigenereUiState(
    val keyword: String = DEFAULT_KEYWORD,
    val isKeywordValid: Boolean = true,
    val mode: CipherMode = CipherMode.ENCRYPT,
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
        CipherMode.ENCRYPT -> cipher.encrypt(input, key)
        CipherMode.DECRYPT -> cipher.decrypt(input, key)
    }
    return copy(
        isKeywordValid = true,
        output = output,
        alignment = alignLetters(input, output, VigenereUiState.MAX_ALIGNED_LETTERS) { keyword[it % keyword.length] },
    )
}
