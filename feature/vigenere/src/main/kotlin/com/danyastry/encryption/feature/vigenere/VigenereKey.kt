package com.danyastry.encryption.feature.vigenere

data class VigenereKey(val keyword: String) {

    init {
        require(keyword.isNotEmpty()) { "Keyword must not be empty" }
        require(keyword.all { it in 'A'..'Z' }) { "Keyword must contain only letters A-Z, was '$keyword'" }
    }

    internal val shifts: IntArray = IntArray(keyword.length) { keyword[it] - 'A' }

    companion object {
        fun normalize(text: String): String = text.uppercase().filter { it in 'A'..'Z' }
    }
}
