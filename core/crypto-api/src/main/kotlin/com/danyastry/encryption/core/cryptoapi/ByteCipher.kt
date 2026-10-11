package com.danyastry.encryption.core.cryptoapi

interface ByteCipher<K> {
    fun encrypt(plaintext: ByteArray, key: K): ByteArray
    fun decrypt(ciphertext: ByteArray, key: K): ByteArray
}
