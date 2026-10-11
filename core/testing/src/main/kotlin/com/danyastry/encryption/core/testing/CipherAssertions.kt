package com.danyastry.encryption.core.testing

import com.danyastry.encryption.core.cryptoapi.ByteCipher
import com.danyastry.encryption.core.cryptoapi.TextCipher
import com.google.common.truth.Truth.assertThat

fun <K> assertRoundTrip(cipher: TextCipher<K>, plaintext: String, key: K) {
    val ciphertext = cipher.encrypt(plaintext, key)

    assertThat(cipher.decrypt(ciphertext, key)).isEqualTo(plaintext)
}

fun <K> assertRoundTrip(cipher: ByteCipher<K>, plaintext: ByteArray, key: K) {
    val ciphertext = cipher.encrypt(plaintext, key)

    assertThat(cipher.decrypt(ciphertext, key)).isEqualTo(plaintext)
}
