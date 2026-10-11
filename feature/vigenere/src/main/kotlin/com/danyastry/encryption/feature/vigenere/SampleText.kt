package com.danyastry.encryption.feature.vigenere

object SampleText {
    const val KASISKI =
        "The Vigenere cipher was called the indecipherable cipher for three hundred years. It hides letter " +
            "frequencies by using several Caesar shifts in turn, one for each letter of the keyword. In the nineteenth " +
            "century Charles Babbage and Friedrich Kasiski found the weakness. When the same piece of plaintext lines up " +
            "with the same part of the key, the same piece of ciphertext appears again. The distance between these " +
            "repeats is a multiple of the key length. Once the length is known, every column of the message is just a " +
            "Caesar cipher, and each column can be broken by comparing its letter counts with the usual frequencies of " +
            "English text. The longer the message, the easier it gets."
}
