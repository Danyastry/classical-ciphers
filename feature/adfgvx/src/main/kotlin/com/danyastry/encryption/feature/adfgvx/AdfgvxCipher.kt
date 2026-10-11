package com.danyastry.encryption.feature.adfgvx

import com.danyastry.encryption.core.classical.ColumnarTransposition
import com.danyastry.encryption.core.cryptoapi.TextCipher

class AdfgvxCipher : TextCipher<AdfgvxKey> {

    override fun encrypt(plaintext: String, key: AdfgvxKey): String =
        ColumnarTransposition.encrypt(fractionate(plaintext, key), key.transposition)

    override fun decrypt(ciphertext: String, key: AdfgvxKey): String {
        require(isValidCiphertext(ciphertext)) {
            "ADFGVX ciphertext must be an even number of the letters $LABELS, was '$ciphertext'"
        }
        val fractionated = ColumnarTransposition.decrypt(normalizeCiphertext(ciphertext), key.transposition)
        return fractionated.chunked(2).map { pair ->
            key.square.letterAt(LABELS.indexOf(pair[0]), LABELS.indexOf(pair[1]))
        }.joinToString("")
    }

    fun fractionate(plaintext: String, key: AdfgvxKey): String =
        key.square.normalize(plaintext).map { "${LABELS[key.square.rowOf(it)]}${LABELS[key.square.columnOf(it)]}" }
            .joinToString("")

    fun isValidCiphertext(ciphertext: String): Boolean {
        val letters = ciphertext.uppercase().filterNot { it.isWhitespace() }
        return letters.all { it in LABELS } && letters.length % 2 == 0
    }

    private fun normalizeCiphertext(ciphertext: String): String = ciphertext.uppercase().filter { it in LABELS }

    companion object {
        const val LABELS = "ADFGVX"
    }
}
