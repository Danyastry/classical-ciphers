package com.danyastry.encryption.feature.onetimepad.ui

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.onetimepad.OneTimePadCipher

data class OneTimePadUiState(
    val key: String = DEFAULT_KEY,
    val mode: CipherMode = CipherMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
    val lettersNeeded: Int = 0,
    val isKeyTooShort: Boolean = false,
    val alternativePlaintext: String = "",
    val alternativeKey: String = "",
    val isAlternativeLengthMismatch: Boolean = false,
) {
    val ciphertext: String
        get() = if (mode == CipherMode.ENCRYPT) output else input

    companion object {
        const val DEFAULT_KEY = "XMCKL"
    }
}

private val cipher = OneTimePadCipher()

internal fun OneTimePadUiState.recalculated(): OneTimePadUiState {
    val needed = OneTimePadCipher.lettersIn(input)
    val tooShort = key.length < needed
    val output = when {
        tooShort -> ""
        key.isEmpty() -> input
        mode == CipherMode.ENCRYPT -> cipher.encrypt(input, Keyword(key))
        else -> cipher.decrypt(input, Keyword(key))
    }
    val withOutput = copy(output = output, lettersNeeded = needed, isKeyTooShort = tooShort)
    return withOutput.withAlternative()
}

private fun OneTimePadUiState.withAlternative(): OneTimePadUiState {
    val cipherLetters = OneTimePadCipher.lettersIn(ciphertext)
    val alternativeLetters = OneTimePadCipher.lettersIn(alternativePlaintext)
    if (alternativeLetters == 0 || cipherLetters == 0) {
        return copy(alternativeKey = "", isAlternativeLengthMismatch = false)
    }
    if (alternativeLetters != cipherLetters) {
        return copy(alternativeKey = "", isAlternativeLengthMismatch = true)
    }
    return copy(alternativeKey = cipher.keyFor(ciphertext, alternativePlaintext), isAlternativeLengthMismatch = false)
}
