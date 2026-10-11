package com.danyastry.encryption.feature.atbash

import com.danyastry.encryption.core.classical.ALPHABET_SIZE
import com.danyastry.encryption.core.classical.mapLetters

class AtbashCipher {

    fun transform(text: String): String = text.mapLetters { ALPHABET_SIZE - 1 - it }
}
