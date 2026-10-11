package com.danyastry.encryption.feature.substitution

import com.danyastry.encryption.core.classical.LetterFrequency

object FrequencyAttack {

    fun guessKey(ciphertext: String): SubstitutionKey {
        val cipherOrder = LetterFrequency.byFrequency(ciphertext)
        val alphabet = CharArray(cipherOrder.length)
        LetterFrequency.englishByFrequency.forEachIndexed { rank, plain -> alphabet[plain - 'A'] = cipherOrder[rank] }
        return SubstitutionKey(String(alphabet))
    }

    fun swapPlaintextLetters(key: SubstitutionKey, first: Char, second: Char): SubstitutionKey {
        val alphabet = key.alphabet.toCharArray()
        val a = first.uppercaseChar() - 'A'
        val b = second.uppercaseChar() - 'A'
        require(a in alphabet.indices && b in alphabet.indices) { "Swap needs two letters A-Z, was '$first' and '$second'" }
        alphabet[a] = alphabet[b].also { alphabet[b] = alphabet[a] }
        return SubstitutionKey(String(alphabet))
    }
}
