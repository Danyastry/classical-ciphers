package com.danyastry.encryption.feature.rot.ui

import com.danyastry.encryption.feature.rot.RotCipher
import com.danyastry.encryption.feature.rot.RotVariant

data class RotUiState(
    val variant: RotVariant = RotVariant.ROT13,
    val input: String = "",
    val output: String = "",
)

internal fun RotUiState.recalculated(): RotUiState =
    copy(output = RotCipher(variant).transform(input))
