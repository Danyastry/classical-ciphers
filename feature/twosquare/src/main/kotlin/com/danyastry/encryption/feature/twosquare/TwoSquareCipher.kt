package com.danyastry.encryption.feature.twosquare

import com.danyastry.encryption.core.classical.PolybiusSquare
import com.danyastry.encryption.core.cryptoapi.TextCipher

enum class SquareAlphabet(val letters: String) {
    MERGE_I_J(PolybiusSquare.LATIN_25),
    OMIT_Q(PolybiusSquare.LATIN_25_NO_Q),
}

data class TwoSquareKey(val top: PolybiusSquare, val bottom: PolybiusSquare) {

    init {
        require(top.alphabet == bottom.alphabet) { "Both squares must use the same alphabet" }
        require(top.size == SIZE) { "Two-square needs 5x5 squares, was ${top.size}x${top.size}" }
    }

    companion object {
        const val SIZE = 5

        fun fromKeywords(first: String, second: String, alphabet: SquareAlphabet = SquareAlphabet.MERGE_I_J) =
            TwoSquareKey(
                top = PolybiusSquare.fromKeyword(first, alphabet.letters),
                bottom = PolybiusSquare.fromKeyword(second, alphabet.letters),
            )
    }
}

data class TwoSquareStep(val input: String, val output: String) {
    val isTransparency: Boolean
        get() = input == output
}

class TwoSquareCipher : TextCipher<TwoSquareKey> {

    override fun encrypt(plaintext: String, key: TwoSquareKey): String = steps(plaintext, key).joinToString("") { it.output }

    override fun decrypt(ciphertext: String, key: TwoSquareKey): String {
        require(key.top.normalize(ciphertext).length % 2 == 0) { "Two-square ciphertext must have an even number of letters" }
        return encrypt(ciphertext, key)
    }

    fun steps(text: String, key: TwoSquareKey): List<TwoSquareStep> = digraphs(text, key).map { pair ->
        val rowA = key.top.rowOf(pair[0])
        val columnA = key.top.columnOf(pair[0])
        val rowB = key.bottom.rowOf(pair[1])
        val columnB = key.bottom.columnOf(pair[1])
        val output = if (columnA == columnB) pair else "${key.top.letterAt(rowA, columnB)}${key.bottom.letterAt(rowB, columnA)}"
        TwoSquareStep(input = pair, output = output)
    }

    fun digraphs(text: String, key: TwoSquareKey): List<String> {
        val letters = key.top.normalize(text)
        return (if (letters.length % 2 == 0) letters else letters + PADDING).chunked(2)
    }

    companion object {
        const val PADDING = 'X'
    }
}
