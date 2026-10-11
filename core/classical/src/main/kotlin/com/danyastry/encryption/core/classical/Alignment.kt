package com.danyastry.encryption.core.classical

data class AlignedLetter(val input: Char, val key: Char, val output: Char)

fun alignLetters(
    input: String,
    output: String,
    limit: Int,
    keyAt: (letterIndex: Int) -> Char,
): List<AlignedLetter> =
    input.indices
        .filter { input[it].isAsciiLetter() }
        .take(limit)
        .mapIndexed { letterIndex, position ->
            AlignedLetter(input = input[position], key = keyAt(letterIndex), output = output[position])
        }
