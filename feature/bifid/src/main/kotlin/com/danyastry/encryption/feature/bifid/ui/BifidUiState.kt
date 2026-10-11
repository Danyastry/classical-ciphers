package com.danyastry.encryption.feature.bifid.ui

import com.danyastry.encryption.core.classical.PolybiusSquare
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.bifid.BifidCipher
import com.danyastry.encryption.feature.bifid.BifidKey

data class Coordinates(val letter: Char, val row: Int, val column: Int)

data class BifidUiState(
    val keyword: String = DEFAULT_KEYWORD,
    val square: List<String> = PolybiusSquare.fromKeyword(DEFAULT_KEYWORD).rows,
    val period: Int = BifidKey.WHOLE_MESSAGE,
    val mode: CipherMode = CipherMode.ENCRYPT,
    val input: String = "",
    val output: String = "",
    val coordinates: List<Coordinates> = emptyList(),
) {
    companion object {
        const val DEFAULT_KEYWORD = "BGWKZQPNDSIOAXEFCLUMTHYVR"
        const val MAX_PERIOD = 10
        const val MAX_COORDINATES = 32
    }
}

private val cipher = BifidCipher()

internal fun BifidUiState.recalculated(): BifidUiState {
    val square = PolybiusSquare.fromKeyword(keyword)
    val key = BifidKey(square, period)
    val output = when (mode) {
        CipherMode.ENCRYPT -> cipher.encrypt(input, key)
        CipherMode.DECRYPT -> cipher.decrypt(input, key)
    }
    val plaintext = if (mode == CipherMode.ENCRYPT) square.normalize(input) else output
    return copy(
        square = square.rows,
        output = output,
        coordinates = plaintext.take(BifidUiState.MAX_COORDINATES).map {
            Coordinates(letter = it, row = square.rowOf(it) + 1, column = square.columnOf(it) + 1)
        },
    )
}
