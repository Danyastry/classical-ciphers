package com.danyastry.encryption.feature.rot

enum class RotVariant { ROT13, ROT47 }

class RotCipher(private val variant: RotVariant) {

    fun transform(text: String): String = buildString(text.length) {
        text.forEach { append(rotate(it)) }
    }

    private fun rotate(char: Char): Char = when (variant) {
        RotVariant.ROT13 -> when (char) {
            in 'a'..'z' -> 'a' + (char - 'a' + 13) % 26
            in 'A'..'Z' -> 'A' + (char - 'A' + 13) % 26
            else -> char
        }
        RotVariant.ROT47 -> when (char) {
            in '!'..'~' -> '!' + (char - '!' + 47) % 94
            else -> char
        }
    }
}
