package com.danyastry.encryption.feature.beaufort

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.classical.mapLetters
import com.danyastry.encryption.core.cryptoapi.TextCipher

class BeaufortCipher : TextCipher<Keyword> {

    override fun encrypt(plaintext: String, key: Keyword): String = transform(plaintext, key)

    override fun decrypt(ciphertext: String, key: Keyword): String = transform(ciphertext, key)

    private fun transform(text: String, key: Keyword): String {
        var position = 0
        return text.mapLetters { key.shiftAt(position++) - it }
    }
}
