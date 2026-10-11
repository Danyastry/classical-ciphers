package com.danyastry.encryption.feature.affine.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.affine.AffineCipher
import com.danyastry.encryption.feature.affine.AffineKey

data class AffineUiState(
    val key: AffineKey = AffineKey(a = 5, b = 8),
    val mode: CipherMode = CipherMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
)

private val cipher = AffineCipher()

internal fun AffineUiState.recalculated(): AffineUiState = copy(
    output = when (mode) {
        CipherMode.ENCRYPT -> cipher.encrypt(input, key)
        CipherMode.DECRYPT -> cipher.decrypt(input, key)
    },
)
