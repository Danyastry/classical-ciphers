package com.danyastry.encryption.core.classical

data class Keyword(val letters: String) {

    init {
        require(letters.isNotEmpty()) { "Keyword must not be empty" }
        require(letters.all { it in 'A'..'Z' }) { "Keyword must contain only letters A-Z, was '$letters'" }
    }

    val length: Int
        get() = letters.length

    fun shiftAt(position: Int): Int = letters[position % letters.length] - 'A'

    companion object {
        fun normalize(text: String): String = text.uppercase().filter { it in 'A'..'Z' }
    }
}
