package com.danyastry.encryption.feature.hill.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.hill.HillCipher
import com.danyastry.encryption.feature.hill.HillKey
import com.danyastry.encryption.feature.hill.HillKeyError
import com.danyastry.encryption.feature.hill.HillMatrix

data class HillUiState(
    val size: Int = 2,
    val keyText: String = DEFAULT_KEY,
    val keyError: HillKeyError? = null,
    val keyMatrix: List<List<Int>> = emptyList(),
    val inverseMatrix: List<List<Int>> = emptyList(),
    val determinant: Int? = null,
    val mode: CipherMode = CipherMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
    val isIncompleteCiphertext: Boolean = false,
) {
    companion object {
        const val DEFAULT_KEY = "HILL"
        const val DEFAULT_KEY_3X3 = "GYBNQKURP"
    }
}

private val cipher = HillCipher()

internal fun HillUiState.recalculated(): HillUiState {
    val error = HillKey.validate(keyText, size)
    if (error == HillKeyError.WRONG_LENGTH) {
        return copy(
            keyError = error,
            keyMatrix = emptyList(),
            inverseMatrix = emptyList(),
            determinant = null,
            output = "",
            isIncompleteCiphertext = false,
        )
    }

    val matrix = HillMatrix.fromLetters(keyText, size)
    if (error == HillKeyError.NOT_INVERTIBLE) {
        return copy(
            keyError = error,
            keyMatrix = matrix.rows,
            inverseMatrix = emptyList(),
            determinant = matrix.determinant,
            output = "",
            isIncompleteCiphertext = false,
        )
    }

    val key = HillKey(matrix)
    val isIncomplete = mode == CipherMode.DECRYPT && HillCipher.normalize(input).length % size != 0
    return copy(
        keyError = null,
        keyMatrix = matrix.rows,
        inverseMatrix = key.inverse.rows,
        determinant = matrix.determinant,
        output = when {
            isIncomplete -> ""
            mode == CipherMode.ENCRYPT -> cipher.encrypt(input, key)
            else -> cipher.decrypt(input, key)
        },
        isIncompleteCiphertext = isIncomplete,
    )
}
