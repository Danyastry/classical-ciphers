package com.danyastry.encryption.feature.atbash

class AtbashCipher {

    fun transform(text: String): String = buildString(text.length) {
        text.forEach { append(mirror(it)) }
    }

    private fun mirror(char: Char): Char = when (char) {
        in 'a'..'z' -> 'z' - (char - 'a')
        in 'A'..'Z' -> 'Z' - (char - 'A')
        else -> char
    }
}
