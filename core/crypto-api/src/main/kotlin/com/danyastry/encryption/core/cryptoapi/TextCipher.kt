package com.danyastry.encryption.core.cryptoapi

interface TextCipher<K> {
    fun encrypt(plaintext: String, key: K): String
    fun decrypt(ciphertext: String, key: K): String
}
