package com.danyastry.encryption.feature.vigenere

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.classical.mapLetters
import com.danyastry.encryption.core.cryptoapi.TextCipher

class VigenereCipher : TextCipher<Keyword> {

    override fun encrypt(plaintext: String, key: Keyword): String {
        var position = 0
        return plaintext.mapLetters { it + key.shiftAt(position++) }
    }

    override fun decrypt(ciphertext: String, key: Keyword): String {
        var position = 0
        return ciphertext.mapLetters { it - key.shiftAt(position++) }
    }
}
