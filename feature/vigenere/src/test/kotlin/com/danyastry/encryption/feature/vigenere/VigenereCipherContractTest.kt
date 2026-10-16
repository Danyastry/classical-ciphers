package com.danyastry.encryption.feature.vigenere

import com.danyastry.encryption.core.testing.VigenereCipherContract

class VigenereCipherContractTest : VigenereCipherContract() {
    override val cipher = VigenereCipher()
}
