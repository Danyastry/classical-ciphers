package com.danyastry.encryption.feature.trifid.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.trifid.TrifidCipher
import com.danyastry.encryption.feature.trifid.TrifidCube
import com.danyastry.encryption.feature.trifid.TrifidKey

data class CubeCoordinates(val letter: Char, val layer: Int, val row: Int, val column: Int)

data class TrifidUiState(
    val keyword: String = DEFAULT_KEYWORD,
    val layers: List<List<String>> = TrifidCube.fromKeyword(DEFAULT_KEYWORD).layers,
    val period: Int = TrifidKey.DEFAULT_PERIOD,
    val mode: CipherMode = CipherMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
    val coordinates: List<CubeCoordinates> = emptyList(),
) {
    companion object {
        const val DEFAULT_KEYWORD = "FELIX MARIE DELASTELLE"
        const val MAX_PERIOD = 10
        const val MAX_COORDINATES = 32
    }
}

private val cipher = TrifidCipher()

internal fun TrifidUiState.recalculated(): TrifidUiState {
    val cube = TrifidCube.fromKeyword(keyword)
    val key = TrifidKey(cube, period)
    val output = when (mode) {
        CipherMode.ENCRYPT -> cipher.encrypt(input, key)
        CipherMode.DECRYPT -> cipher.decrypt(input, key)
    }
    val plaintext = if (mode == CipherMode.ENCRYPT) TrifidCube.normalize(input) else output
    return copy(
        layers = cube.layers,
        output = output,
        coordinates = plaintext.take(TrifidUiState.MAX_COORDINATES).map { letter ->
            val (layer, row, column) = cube.coordinatesOf(letter)
            CubeCoordinates(letter, layer = layer + 1, row = row + 1, column = column + 1)
        },
    )
}
