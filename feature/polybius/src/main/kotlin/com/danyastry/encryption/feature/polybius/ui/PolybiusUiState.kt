package com.danyastry.encryption.feature.polybius.ui

import com.danyastry.encryption.core.classical.PolybiusSquare
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.polybius.PolybiusCipher

data class PolybiusUiState(
    val keyword: String = "",
    val square: List<String> = PolybiusSquare.fromKeyword().rows,
    val mode: CipherMode = CipherMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
    val isInvalidCode: Boolean = false,
)

private val cipher = PolybiusCipher()

internal fun PolybiusUiState.recalculated(): PolybiusUiState {
    val square = PolybiusSquare.fromKeyword(keyword)
    val isInvalid = mode == CipherMode.DECRYPT && !cipher.isValidCode(input, square)
    return copy(
        square = square.rows,
        output = when {
            isInvalid -> ""
            mode == CipherMode.ENCRYPT -> cipher.encrypt(input, square)
            else -> cipher.decrypt(input, square)
        },
        isInvalidCode = isInvalid,
    )
}
