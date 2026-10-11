package com.danyastry.encryption.feature.autokey

import com.danyastry.encryption.core.cryptoapi.TextCipher

class AutokeyCipher : TextCipher<AutokeyKey> {

    override fun encrypt(plaintext: String, key: AutokeyKey): String = transform(plaintext, key, decrypting = false)

    override fun decrypt(ciphertext: String, key: AutokeyKey): String = transform(ciphertext, key, decrypting = true)

    fun keystream(plaintext: String, key: AutokeyKey): String {
        val letters = plaintext.filter { it in 'a'..'z' || it in 'A'..'Z' }.uppercase()
        return (key.primer + letters).take(letters.length)
    }

    private fun transform(text: String, key: AutokeyKey, decrypting: Boolean): String {
        val keystream = ArrayDeque(key.primer.map { it - 'A' })
        return buildString(text.length) {
            text.forEach { char ->
                val base = when (char) {
                    in 'a'..'z' -> 'a'
                    in 'A'..'Z' -> 'A'
                    else -> null
                }
                if (base == null) {
                    append(char)
                } else {
                    val input = char - base
                    val shift = keystream.removeFirst()
                    val output = if (decrypting) (input - shift).mod(ALPHABET_SIZE) else (input + shift) % ALPHABET_SIZE
                    keystream.addLast(if (decrypting) output else input)
                    append(base + output)
                }
            }
        }
    }

    private companion object {
        const val ALPHABET_SIZE = 26
    }
}
