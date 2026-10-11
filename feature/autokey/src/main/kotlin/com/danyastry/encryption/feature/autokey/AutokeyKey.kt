package com.danyastry.encryption.feature.autokey

data class AutokeyKey(val primer: String) {

    init {
        require(primer.isNotEmpty()) { "Primer must not be empty" }
        require(primer.all { it in 'A'..'Z' }) { "Primer must contain only letters A-Z, was '$primer'" }
    }

    companion object {
        fun normalize(text: String): String = text.uppercase().filter { it in 'A'..'Z' }
    }
}
