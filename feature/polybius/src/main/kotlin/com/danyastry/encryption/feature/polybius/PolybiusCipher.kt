package com.danyastry.encryption.feature.polybius

import com.danyastry.encryption.core.classical.PolybiusSquare
import com.danyastry.encryption.core.cryptoapi.TextCipher

class PolybiusCipher : TextCipher<PolybiusSquare> {

    override fun encrypt(plaintext: String, key: PolybiusSquare): String =
        key.normalize(plaintext).map { "${key.rowOf(it) + 1}${key.columnOf(it) + 1}" }.joinToString(" ")

    override fun decrypt(ciphertext: String, key: PolybiusSquare): String {
        val digits = ciphertext.filter { it.isDigit() }
        require(isValidCode(ciphertext, key)) {
            "Polybius code must be pairs of digits from 1 to ${key.size}, was '$ciphertext'"
        }
        return digits.chunked(2).map { key.letterAt(it[0] - '1', it[1] - '1') }.joinToString("")
    }

    fun isValidCode(ciphertext: String, key: PolybiusSquare): Boolean {
        val digits = ciphertext.filter { it.isDigit() }
        return digits.length % 2 == 0 && digits.all { it in '1'..('0' + key.size) }
    }
}
