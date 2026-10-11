package com.danyastry.encryption.feature.rot

import com.danyastry.encryption.core.classical.mapLetters

enum class RotVariant { ROT13, ROT47 }

class RotCipher(private val variant: RotVariant) {

    fun transform(text: String): String = when (variant) {
        RotVariant.ROT13 -> text.mapLetters { it + 13 }
        RotVariant.ROT47 -> buildString(text.length) { text.forEach { append(rot47(it)) } }
    }

    private fun rot47(char: Char): Char = when (char) {
        in '!'..'~' -> '!' + (char - '!' + 47) % 94
        else -> char
    }
}
