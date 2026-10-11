package com.danyastry.encryption.feature.trifid

import com.danyastry.encryption.core.cryptoapi.TextCipher

data class TrifidKey(val cube: TrifidCube, val period: Int = DEFAULT_PERIOD) {

    init {
        require(period >= 0) { "Period must be 0 for the whole message or positive, was $period" }
    }

    companion object {
        const val WHOLE_MESSAGE = 0
        const val DEFAULT_PERIOD = 5
    }
}

class TrifidCipher : TextCipher<TrifidKey> {

    override fun encrypt(plaintext: String, key: TrifidKey): String =
        blocks(TrifidCube.normalize(plaintext), key.period).joinToString("") { block ->
            val coordinates = block.map { key.cube.coordinatesOf(it) }
            val sequence = coordinates.map { it.first } + coordinates.map { it.second } + coordinates.map { it.third }
            sequence.chunked(3).map { (layer, row, column) -> key.cube.letterAt(layer, row, column) }.joinToString("")
        }

    override fun decrypt(ciphertext: String, key: TrifidKey): String =
        blocks(TrifidCube.normalize(ciphertext), key.period).joinToString("") { block ->
            val sequence = block.flatMap { key.cube.coordinatesOf(it).toList() }
            val length = block.length
            block.indices.map { index ->
                key.cube.letterAt(sequence[index], sequence[length + index], sequence[2 * length + index])
            }.joinToString("")
        }

    private fun blocks(letters: String, period: Int): List<String> =
        if (period == TrifidKey.WHOLE_MESSAGE || letters.isEmpty()) listOf(letters) else letters.chunked(period)
}
