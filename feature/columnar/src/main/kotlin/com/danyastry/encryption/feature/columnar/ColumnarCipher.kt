package com.danyastry.encryption.feature.columnar

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.cryptoapi.TextCipher

class ColumnarCipher(private val padding: Char? = null) : TextCipher<Keyword> {

    override fun encrypt(plaintext: String, key: Keyword): String {
        val columns = key.length
        val text = padded(plaintext, columns)
        return buildString(text.length) {
            columnOrder(key).forEach { column ->
                for (index in column until text.length step columns) append(text[index])
            }
        }
    }

    override fun decrypt(ciphertext: String, key: Keyword): String {
        val columns = key.length
        val fullRows = ciphertext.length / columns
        val longColumns = ciphertext.length % columns
        val columnTexts = arrayOfNulls<String>(columns)
        var start = 0
        columnOrder(key).forEach { column ->
            val length = fullRows + if (column < longColumns) 1 else 0
            columnTexts[column] = ciphertext.substring(start, start + length)
            start += length
        }
        return buildString(ciphertext.length) {
            for (row in 0..fullRows) {
                columnTexts.forEach { text -> text?.getOrNull(row)?.let(::append) }
            }
        }
    }

    fun padded(plaintext: String, columns: Int): String {
        val pad = padding ?: return plaintext
        val missing = (columns - plaintext.length % columns) % columns
        return plaintext + pad.toString().repeat(missing)
    }

    companion object {
        fun columnOrder(key: Keyword): List<Int> =
            key.letters.indices.sortedWith(compareBy({ key.letters[it] }, { it }))
    }
}
