package com.danyastry.encryption.feature.autokey.ui

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.classical.isAsciiLetter
import com.danyastry.encryption.feature.autokey.AutokeyCipher

enum class AutokeyMode { ENCRYPT, DECRYPT }

data class AlignedLetter(val input: Char, val key: Char, val output: Char, val isPrimer: Boolean)

data class AutokeyUiState(
    val primer: String = DEFAULT_PRIMER,
    val isPrimerValid: Boolean = true,
    val mode: AutokeyMode = AutokeyMode.ENCRYPT,
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
        AutokeyMode.ENCRYPT -> cipher.encrypt(input, key)
        AutokeyMode.DECRYPT -> cipher.decrypt(input, key)
    }
    val plaintext = if (mode == AutokeyMode.ENCRYPT) input else output
    return copy(
        isPrimerValid = true,
        output = output,
        alignment = align(input, output, cipher.keystream(plaintext, key), primer.length),
    )
}

private fun align(input: String, output: String, keystream: String, primerLength: Int): List<AlignedLetter> =
    input.indices
        .filter { input[it].isAsciiLetter() }
        .take(AutokeyUiState.MAX_ALIGNED_LETTERS)
        .mapIndexed { letterIndex, position ->
            AlignedLetter(
                input = input[position],
                key = keystream[letterIndex],
                output = output[position],
                isPrimer = letterIndex < primerLength,
            )
        }
