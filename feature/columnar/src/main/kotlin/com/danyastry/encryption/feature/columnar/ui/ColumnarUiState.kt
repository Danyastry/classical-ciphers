package com.danyastry.encryption.feature.columnar.ui

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.columnar.ColumnarCipher

data class ColumnarUiState(
    val keyword: String = DEFAULT_KEYWORD,
    val isKeywordValid: Boolean = true,
    val padWithX: Boolean = false,
    val mode: CipherMode = CipherMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
    val readingOrder: String = "",
    val grid: List<String> = emptyList(),
) {
    companion object {
        const val DEFAULT_KEYWORD = "ZEBRAS"
        const val PADDING = 'X'
        const val MAX_GRID_ROWS = 12
    }
}

internal fun ColumnarUiState.recalculated(): ColumnarUiState {
    if (keyword.isEmpty()) {
        return copy(isKeywordValid = false, output = "", readingOrder = "", grid = emptyList())
    }

    val key = Keyword(keyword)
    val cipher = ColumnarCipher(padding = if (padWithX) ColumnarUiState.PADDING else null)
    val output = when (mode) {
        CipherMode.ENCRYPT -> cipher.encrypt(input, key)
        CipherMode.DECRYPT -> cipher.decrypt(input, key)
    }
    val plaintext = if (mode == CipherMode.ENCRYPT) cipher.padded(input, key.length) else output
    return copy(
        isKeywordValid = true,
        output = output,
        readingOrder = ColumnarCipher.columnOrder(key).map { keyword[it] }.joinToString(" "),
        grid = plaintext.chunked(key.length).take(ColumnarUiState.MAX_GRID_ROWS),
    )
}
