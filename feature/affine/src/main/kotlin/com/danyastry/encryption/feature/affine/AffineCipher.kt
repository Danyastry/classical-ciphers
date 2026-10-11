package com.danyastry.encryption.feature.affine

import com.danyastry.encryption.core.cryptoapi.TextCipher
import com.danyastry.encryption.feature.affine.AffineKey.Companion.ALPHABET_SIZE

class AffineCipher : TextCipher<AffineKey> {

    override fun encrypt(plaintext: String, key: AffineKey): String {
        val a = key.a.mod(ALPHABET_SIZE)
        val b = key.b.mod(ALPHABET_SIZE)
        return mapLetters(plaintext) { x -> (a * x + b) % ALPHABET_SIZE }
    }

    override fun decrypt(ciphertext: String, key: AffineKey): String {
        val inverse = key.inverseOfA
        val b = key.b.mod(ALPHABET_SIZE)
        return mapLetters(ciphertext) { y -> (inverse * (y - b + ALPHABET_SIZE)) % ALPHABET_SIZE }
    }

    private inline fun mapLetters(text: String, transform: (Int) -> Int): String = buildString(text.length) {
        text.forEach { char ->
            append(
                when (char) {
                    in 'a'..'z' -> 'a' + transform(char - 'a')
                    in 'A'..'Z' -> 'A' + transform(char - 'A')
                    else -> char
                },
            )
        }
    }
}
