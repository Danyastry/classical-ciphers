package com.danyastry.encryption.feature.affine.ui

import com.danyastry.encryption.feature.affine.AffineCipher
import com.danyastry.encryption.feature.affine.AffineKey

enum class AffineMode { ENCRYPT, DECRYPT }

data class AffineUiState(
    val key: AffineKey = AffineKey(a = 5, b = 8),
    val mode: AffineMode = AffineMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
)

private val cipher = AffineCipher()

internal fun AffineUiState.recalculated(): AffineUiState = copy(
    output = when (mode) {
        AffineMode.ENCRYPT -> cipher.encrypt(input, key)
        AffineMode.DECRYPT -> cipher.decrypt(input, key)
    },
)
