package com.danyastry.encryption.feature.adfgvx

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.classical.PolybiusSquare
import kotlin.random.Random

data class AdfgvxKey(val square: PolybiusSquare, val transposition: Keyword) {

    init {
        require(square.size == AdfgvxCipher.LABELS.length) {
            "ADFGVX needs a ${AdfgvxCipher.LABELS.length}x${AdfgvxCipher.LABELS.length} square, was ${square.size}x${square.size}"
        }
    }

    companion object {
        fun squareFrom(keyword: String): PolybiusSquare =
            PolybiusSquare.fromKeyword(keyword, PolybiusSquare.ALPHANUMERIC_36)

        fun randomSquareKeyword(random: Random): String =
            PolybiusSquare.ALPHANUMERIC_36.toList().shuffled(random).joinToString("")
    }
}
