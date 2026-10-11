package com.danyastry.encryption.feature.autokey.ui

import com.danyastry.encryption.core.classical.AlignedLetter
import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.classical.alignLetters
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.autokey.AutokeyCipher

data class AutokeyUiState(
    val primer: String = DEFAULT_PRIMER,
    val isPrimerValid: Boolean = true,
    val mode: CipherMode = CipherMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
    val alignment: List<AlignedLetter> = emptyList(),
) {
    companion object {
        const val DEFAULT_PRIMER = "QUEENLY"
        const val MAX_ALIGNED_LETTERS = 32
    }
}

private val cipher = AutokeyCipher()

internal fun AutokeyUiState.recalculated(): AutokeyUiState {
    if (primer.isEmpty()) return copy(isPrimerValid = false, output = "", alignment = emptyList())

    val key = Keyword(primer)
    val output = when (mode) {
        CipherMode.ENCRYPT -> cipher.encrypt(input, key)
        CipherMode.DECRYPT -> cipher.decrypt(input, key)
    }
    val keystream = cipher.keystream(if (mode == CipherMode.ENCRYPT) input else output, key)
    return copy(
        isPrimerValid = true,
        output = output,
        alignment = alignLetters(input, output, AutokeyUiState.MAX_ALIGNED_LETTERS) { keystream[it] },
    )
}
