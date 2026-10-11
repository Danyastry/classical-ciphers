package com.danyastry.encryption.feature.substitution

import com.danyastry.encryption.core.classical.mapLetters
import com.danyastry.encryption.core.cryptoapi.TextCipher

class SubstitutionCipher : TextCipher<SubstitutionKey> {

    override fun encrypt(plaintext: String, key: SubstitutionKey): String =
        plaintext.mapLetters { key.alphabet[it] - 'A' }

    override fun decrypt(ciphertext: String, key: SubstitutionKey): String =
        ciphertext.mapLetters { key.inverse[it] }
}
