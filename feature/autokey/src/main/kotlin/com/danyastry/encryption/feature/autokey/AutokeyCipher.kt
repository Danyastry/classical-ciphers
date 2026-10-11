package com.danyastry.encryption.feature.autokey

import com.danyastry.encryption.core.classical.ALPHABET_SIZE
import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.classical.asciiLetters
import com.danyastry.encryption.core.classical.mapLetters
import com.danyastry.encryption.core.cryptoapi.TextCipher

class AutokeyCipher : TextCipher<Keyword> {

    override fun encrypt(plaintext: String, key: Keyword): String {
        val keystream = keystreamQueue(key)
        return plaintext.mapLetters { plain ->
            val shift = keystream.removeFirst()
            keystream.addLast(plain)
            plain + shift
        }
    }

    override fun decrypt(ciphertext: String, key: Keyword): String {
        val keystream = keystreamQueue(key)
        return ciphertext.mapLetters { cipher ->
            val plain = (cipher - keystream.removeFirst()).mod(ALPHABET_SIZE)
            keystream.addLast(plain)
            plain
        }
    }

    fun keystream(plaintext: String, key: Keyword): String {
        val letters = plaintext.asciiLetters().uppercase()
        return (key.letters + letters).take(letters.length)
    }

    private fun keystreamQueue(key: Keyword): ArrayDeque<Int> = ArrayDeque(key.letters.map { it - 'A' })
}
