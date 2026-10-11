package com.danyastry.encryption.feature.bacon.ui

import com.danyastry.encryption.core.classical.isAsciiLetter
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.bacon.BaconAlphabet
import com.danyastry.encryption.feature.bacon.BaconCipher

enum class DecodeSource { AB_GROUPS, LETTER_CASE }

data class BaconUiState(
    val alphabet: BaconAlphabet = BaconAlphabet.CLASSIC_24,
    val mode: CipherMode = CipherMode.ENCRYPT,
    val decodeSource: DecodeSource = DecodeSource.AB_GROUPS,
    val input: String = "",
    val output: String = "",
    val isIncompleteCode: Boolean = false,
    val cover: String = DEFAULT_COVER,
    val stegotext: String = "",
    val requiredCoverLetters: Int = 0,
    val coverLetters: Int = DEFAULT_COVER.count { it.isAsciiLetter() },
) {
    val isCoverTooShort: Boolean
        get() = coverLetters < requiredCoverLetters

    companion object {
        const val DEFAULT_COVER =
            "Francis Bacon wrote about this method in 1605. Any ordinary sentence can carry a secret " +
                "message if the reader knows that the shape of every letter matters more than its meaning."
    }
}

internal fun BaconUiState.recalculated(): BaconUiState {
    val cipher = BaconCipher(alphabet)
    val coverLetters = cover.count { it.isAsciiLetter() }
    return when (mode) {
        CipherMode.ENCRYPT -> {
            val required = cipher.requiredCoverLetters(input)
            copy(
                output = cipher.encode(input),
                isIncompleteCode = false,
                requiredCoverLetters = required,
                coverLetters = coverLetters,
                stegotext = if (required in 1..coverLetters) cipher.hide(input, cover) else "",
            )
        }
        CipherMode.DECRYPT -> {
            val symbols = input.uppercase().count { it == 'A' || it == 'B' }
            val isIncomplete = decodeSource == DecodeSource.AB_GROUPS && symbols % BaconCipher.GROUP_SIZE != 0
            copy(
                output = when {
                    isIncomplete -> ""
                    decodeSource == DecodeSource.AB_GROUPS -> cipher.decode(input)
                    else -> cipher.reveal(input)
                },
                isIncompleteCode = isIncomplete,
                requiredCoverLetters = 0,
                coverLetters = coverLetters,
                stegotext = "",
            )
        }
    }
}
