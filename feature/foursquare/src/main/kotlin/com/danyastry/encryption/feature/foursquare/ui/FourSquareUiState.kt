package com.danyastry.encryption.feature.foursquare.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.foursquare.FourSquareCipher
import com.danyastry.encryption.feature.foursquare.FourSquareKey
import com.danyastry.encryption.feature.foursquare.SquareAlphabet

data class FourSquareUiState(
    val firstKeyword: String = DEFAULT_FIRST,
    val secondKeyword: String = DEFAULT_SECOND,
    val alphabet: SquareAlphabet = SquareAlphabet.OMIT_Q,
    val plainSquare: List<String> = emptyList(),
    val upperRightSquare: List<String> = emptyList(),
    val lowerLeftSquare: List<String> = emptyList(),
    val mode: CipherMode = CipherMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
    val isOddCiphertext: Boolean = false,
) {
    companion object {
        const val DEFAULT_FIRST = "EXAMPLE"
        const val DEFAULT_SECOND = "KEYWORD"
    }
}

private val cipher = FourSquareCipher()

internal fun FourSquareUiState.recalculated(): FourSquareUiState {
    val key = FourSquareKey.fromKeywords(firstKeyword, secondKeyword, alphabet)
    val isOdd = mode == CipherMode.DECRYPT && key.plain.normalize(input).length % 2 != 0
    return copy(
        plainSquare = key.plain.rows,
        upperRightSquare = key.upperRight.rows,
        lowerLeftSquare = key.lowerLeft.rows,
        output = when {
            isOdd -> ""
            mode == CipherMode.ENCRYPT -> cipher.encrypt(input, key).chunked(2).joinToString(" ")
            else -> cipher.decrypt(input, key)
        },
        isOddCiphertext = isOdd,
    )
}
