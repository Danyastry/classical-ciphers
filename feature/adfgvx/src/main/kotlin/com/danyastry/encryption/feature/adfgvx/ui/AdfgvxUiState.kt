package com.danyastry.encryption.feature.adfgvx.ui

import com.danyastry.encryption.core.classical.ColumnarTransposition
import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.adfgvx.AdfgvxCipher
import com.danyastry.encryption.feature.adfgvx.AdfgvxKey

data class AdfgvxUiState(
    val squareKeyword: String = DEFAULT_SQUARE,
    val square: List<String> = AdfgvxKey.squareFrom(DEFAULT_SQUARE).rows,
    val transposition: String = DEFAULT_TRANSPOSITION,
    val isTranspositionValid: Boolean = true,
    val mode: CipherMode = CipherMode.ENCRYPT,
    val input: String = "",
    val fractionated: String = "",
    val output: String = "",
    val isInvalidCiphertext: Boolean = false,
) {
    companion object {
        const val DEFAULT_SQUARE = "NA1C3H8TB2OME5WRPD4F6G7I9J0KLQSUVXYZ"
        const val DEFAULT_TRANSPOSITION = "PRIVACY"
        const val GROUP_SIZE = 5
    }
}

private val cipher = AdfgvxCipher()

internal fun AdfgvxUiState.recalculated(): AdfgvxUiState {
    val square = AdfgvxKey.squareFrom(squareKeyword)
    if (transposition.isEmpty()) {
        return copy(square = square.rows, isTranspositionValid = false, fractionated = "", output = "", isInvalidCiphertext = false)
    }

    val key = AdfgvxKey(square, Keyword(transposition))
    return when (mode) {
        CipherMode.ENCRYPT -> {
            val fractionated = cipher.fractionate(input, key)
            copy(
                square = square.rows,
                isTranspositionValid = true,
                fractionated = fractionated.chunked(2).joinToString(" "),
                output = ColumnarTransposition.encrypt(fractionated, key.transposition).chunked(AdfgvxUiState.GROUP_SIZE).joinToString(" "),
                isInvalidCiphertext = false,
            )
        }
        CipherMode.DECRYPT -> {
            val isInvalid = !cipher.isValidCiphertext(input)
            val letters = input.uppercase().filter { it in AdfgvxCipher.LABELS }
            copy(
                square = square.rows,
                isTranspositionValid = true,
                fractionated = if (isInvalid) "" else ColumnarTransposition.decrypt(letters, key.transposition).chunked(2).joinToString(" "),
                output = if (isInvalid) "" else cipher.decrypt(input, key),
                isInvalidCiphertext = isInvalid,
            )
        }
    }
}
