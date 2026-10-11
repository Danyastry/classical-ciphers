package com.danyastry.encryption.feature.columnar

import com.danyastry.encryption.core.classical.ColumnarTransposition
import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.cryptoapi.TextCipher

class ColumnarCipher(private val padding: Char? = null) : TextCipher<Keyword> {

    override fun encrypt(plaintext: String, key: Keyword): String =
        ColumnarTransposition.encrypt(padded(plaintext, key.length), key)

    override fun decrypt(ciphertext: String, key: Keyword): String = ColumnarTransposition.decrypt(ciphertext, key)

    fun padded(plaintext: String, columns: Int): String {
        val pad = padding ?: return plaintext
        val missing = (columns - plaintext.length % columns) % columns
        return plaintext + pad.toString().repeat(missing)
    }

    companion object {
        fun columnOrder(key: Keyword): List<Int> = ColumnarTransposition.columnOrder(key)
    }
}
