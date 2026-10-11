package com.danyastry.encryption.core.classical

const val ALPHABET_SIZE = 26
const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

fun Char.isAsciiLetter(): Boolean = this in 'a'..'z' || this in 'A'..'Z'

fun String.asciiLetters(): String = filter { it.isAsciiLetter() }

inline fun String.mapLetters(transform: (index: Int) -> Int): String = buildString(length) {
    this@mapLetters.forEach { char ->
        append(
            when (char) {
                in 'a'..'z' -> 'a' + transform(char - 'a').mod(ALPHABET_SIZE)
                in 'A'..'Z' -> 'A' + transform(char - 'A').mod(ALPHABET_SIZE)
                else -> char
            },
        )
    }
}
