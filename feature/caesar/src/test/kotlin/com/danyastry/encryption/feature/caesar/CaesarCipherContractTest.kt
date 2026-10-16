package com.danyastry.encryption.feature.caesar

import com.danyastry.encryption.core.testing.CaesarCipherContract

class CaesarCipherContractTest : CaesarCipherContract() {
    override val cipher = CaesarCipher()
}
