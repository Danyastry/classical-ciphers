package com.danyastry.encryption.feature.substitution.ui

import com.danyastry.encryption.feature.substitution.KeyError
import com.danyastry.encryption.feature.substitution.SubstitutionCipher
import com.danyastry.encryption.feature.substitution.SubstitutionKey

enum class SubstitutionMode { ENCRYPT, DECRYPT }

data class SubstitutionUiState(
    val keyword: String = DEFAULT_KEYWORD,
    val keyText: String = SubstitutionKey.fromKeyword(DEFAULT_KEYWORD).alphabet,
    val keyError: KeyError? = null,
    val mode: SubstitutionMode = SubstitutionMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
) {
    companion object {
        const val DEFAULT_KEYWORD = "ZEBRAS"
    }
}

private val cipher = SubstitutionCipher()

internal fun SubstitutionUiState.recalculated(): SubstitutionUiState {
    val error = SubstitutionKey.validate(keyText)
    if (error != null) return copy(keyError = error, output = "")

    val key = SubstitutionKey(keyText)
    return copy(
        keyError = null,
        output = when (mode) {
            SubstitutionMode.ENCRYPT -> cipher.encrypt(input, key)
            SubstitutionMode.DECRYPT -> cipher.decrypt(input, key)
        },
    )
}
