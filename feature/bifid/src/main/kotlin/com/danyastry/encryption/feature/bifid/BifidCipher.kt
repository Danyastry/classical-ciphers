package com.danyastry.encryption.feature.bifid

import com.danyastry.encryption.core.classical.PolybiusSquare
import com.danyastry.encryption.core.cryptoapi.TextCipher

data class BifidKey(val square: PolybiusSquare, val period: Int = WHOLE_MESSAGE) {

    init {
        require(square.size == 5) { "Bifid needs a 5x5 square, was ${square.size}x${square.size}" }
        require(period >= 0) { "Period must be 0 for the whole message or positive, was $period" }
    }

    companion object {
        const val WHOLE_MESSAGE = 0
    }
}

class BifidCipher : TextCipher<BifidKey> {

    override fun encrypt(plaintext: String, key: BifidKey): String =
        blocks(key.square.normalize(plaintext), key.period).joinToString("") { block ->
            val coordinates = block.map { key.square.rowOf(it) } + block.map { key.square.columnOf(it) }
            coordinates.chunked(2).map { (row, column) -> key.square.letterAt(row, column) }.joinToString("")
        }

    override fun decrypt(ciphertext: String, key: BifidKey): String =
        blocks(key.square.normalize(ciphertext), key.period).joinToString("") { block ->
            val coordinates = block.flatMap { listOf(key.square.rowOf(it), key.square.columnOf(it)) }
            block.indices.map { index -> key.square.letterAt(coordinates[index], coordinates[block.length + index]) }
                .joinToString("")
        }

    private fun blocks(letters: String, period: Int): List<String> =
        if (period == BifidKey.WHOLE_MESSAGE || letters.isEmpty()) listOf(letters) else letters.chunked(period)
}
