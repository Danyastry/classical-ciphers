package com.danyastry.encryption.feature.substitution

import com.danyastry.encryption.core.cryptoapi.TextCipher

class SubstitutionCipher : TextCipher<SubstitutionKey> {

    override fun encrypt(plaintext: String, key: SubstitutionKey): String =
        mapLetters(plaintext) { index -> key.alphabet[index] }

    override fun decrypt(ciphertext: String, key: SubstitutionKey): String =
        mapLetters(ciphertext) { index -> 'A' + key.inverse[index] }

    private inline fun mapLetters(text: String, transform: (Int) -> Char): String = buildString(text.length) {
        text.forEach { char ->
            append(
                when (char) {
                    in 'a'..'z' -> transform(char - 'a').lowercaseChar()
                    in 'A'..'Z' -> transform(char - 'A')
                    else -> char
                },
            )
        }
    }
}
