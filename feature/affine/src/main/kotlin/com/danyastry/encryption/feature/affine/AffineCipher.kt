package com.danyastry.encryption.feature.affine

import com.danyastry.encryption.core.classical.ALPHABET_SIZE
import com.danyastry.encryption.core.classical.mapLetters
import com.danyastry.encryption.core.cryptoapi.TextCipher

class AffineCipher : TextCipher<AffineKey> {

    override fun encrypt(plaintext: String, key: AffineKey): String {
        val a = key.a.mod(ALPHABET_SIZE)
        val b = key.b.mod(ALPHABET_SIZE)
        return plaintext.mapLetters { x -> a * x + b }
    }

    override fun decrypt(ciphertext: String, key: AffineKey): String {
        val inverse = key.inverseOfA
        val b = key.b.mod(ALPHABET_SIZE)
        return ciphertext.mapLetters { y -> inverse * (y - b) }
    }
}
