package com.danyastry.encryption.feature.twosquare.ui

import com.danyastry.encryption.feature.twosquare.SquareAlphabet
import com.danyastry.encryption.feature.twosquare.TwoSquareCipher
import com.danyastry.encryption.feature.twosquare.TwoSquareKey
import com.danyastry.encryption.feature.twosquare.TwoSquareStep

data class TwoSquareUiState(
    val firstKeyword: String = DEFAULT_FIRST,
    val secondKeyword: String = DEFAULT_SECOND,
    val alphabet: SquareAlphabet = SquareAlphabet.OMIT_Q,
    val topSquare: List<String> = emptyList(),
    val bottomSquare: List<String> = emptyList(),
    val input: String = "",
    val output: String = "",
    val steps: List<TwoSquareStep> = emptyList(),
    val transparencies: Int = 0,
    val pairs: Int = 0,
) {
    companion object {
        const val DEFAULT_FIRST = "EXAMPLE"
        const val DEFAULT_SECOND = "KEYWORD"
        const val MAX_STEPS = 16
    }
}

private val cipher = TwoSquareCipher()

internal fun TwoSquareUiState.recalculated(): TwoSquareUiState {
    val key = TwoSquareKey.fromKeywords(firstKeyword, secondKeyword, alphabet)
    val steps = cipher.steps(input, key)
    return copy(
        topSquare = key.top.rows,
        bottomSquare = key.bottom.rows,
        output = steps.joinToString(" ") { it.output },
        steps = steps.take(TwoSquareUiState.MAX_STEPS),
        transparencies = steps.count { it.isTransparency },
        pairs = steps.size,
    )
}
