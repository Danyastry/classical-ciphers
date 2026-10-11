package com.danyastry.encryption.feature.foursquare

import com.danyastry.encryption.core.classical.PolybiusSquare
import com.danyastry.encryption.core.cryptoapi.TextCipher

enum class SquareAlphabet(val letters: String) {
    MERGE_I_J(PolybiusSquare.LATIN_25),
    OMIT_Q(PolybiusSquare.LATIN_25_NO_Q),
}

data class FourSquareKey(val upperRight: PolybiusSquare, val lowerLeft: PolybiusSquare) {

    init {
        require(upperRight.alphabet == lowerLeft.alphabet) { "Both keyed squares must use the same alphabet" }
        require(upperRight.size == SIZE) { "Four-square needs 5x5 squares, was ${upperRight.size}x${upperRight.size}" }
    }

    val plain: PolybiusSquare = PolybiusSquare.fromKeyword(alphabet = upperRight.alphabet)

    companion object {
        const val SIZE = 5

        fun fromKeywords(first: String, second: String, alphabet: SquareAlphabet = SquareAlphabet.MERGE_I_J) =
            FourSquareKey(
                upperRight = PolybiusSquare.fromKeyword(first, alphabet.letters),
                lowerLeft = PolybiusSquare.fromKeyword(second, alphabet.letters),
            )
    }
}

class FourSquareCipher : TextCipher<FourSquareKey> {

    override fun encrypt(plaintext: String, key: FourSquareKey): String =
        digraphs(plaintext, key).joinToString("") { pair ->
            swapCorners(pair, from = key.plain to key.plain, to = key.upperRight to key.lowerLeft)
        }

    override fun decrypt(ciphertext: String, key: FourSquareKey): String {
        val letters = key.plain.normalize(ciphertext)
        require(letters.length % 2 == 0) { "Four-square ciphertext must have an even number of letters, was ${letters.length}" }
        return letters.chunked(2).joinToString("") { pair ->
            swapCorners(pair, from = key.upperRight to key.lowerLeft, to = key.plain to key.plain)
        }
    }

    fun digraphs(plaintext: String, key: FourSquareKey): List<String> {
        val letters = key.plain.normalize(plaintext)
        val padded = if (letters.length % 2 == 0) letters else letters + PADDING
        return padded.chunked(2)
    }

    private fun swapCorners(
        pair: String,
        from: Pair<PolybiusSquare, PolybiusSquare>,
        to: Pair<PolybiusSquare, PolybiusSquare>,
    ): String {
        val (fromTop, fromBottom) = from
        val (toTop, toBottom) = to
        val rowA = fromTop.rowOf(pair[0])
        val columnA = fromTop.columnOf(pair[0])
        val rowB = fromBottom.rowOf(pair[1])
        val columnB = fromBottom.columnOf(pair[1])
        return "${toTop.letterAt(rowA, columnB)}${toBottom.letterAt(rowB, columnA)}"
    }

    companion object {
        const val PADDING = 'X'
    }
}
