package com.danyastry.encryption.core.classical

object ColumnarTransposition {

    fun columnOrder(key: Keyword): List<Int> =
        key.letters.indices.sortedWith(compareBy({ key.letters[it] }, { it }))

    fun encrypt(text: String, key: Keyword): String {
        val columns = key.length
        return buildString(text.length) {
            columnOrder(key).forEach { column ->
                for (index in column until text.length step columns) append(text[index])
            }
        }
    }

    fun decrypt(text: String, key: Keyword): String {
        val columns = key.length
        val fullRows = text.length / columns
        val longColumns = text.length % columns
        val columnTexts = arrayOfNulls<String>(columns)
        var start = 0
        columnOrder(key).forEach { column ->
            val length = fullRows + if (column < longColumns) 1 else 0
            columnTexts[column] = text.substring(start, start + length)
            start += length
        }
        return buildString(text.length) {
            for (row in 0..fullRows) {
                columnTexts.forEach { column -> column?.getOrNull(row)?.let(::append) }
            }
        }
    }
}
