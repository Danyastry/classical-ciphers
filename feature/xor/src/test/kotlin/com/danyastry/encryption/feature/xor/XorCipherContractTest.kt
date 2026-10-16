package com.danyastry.encryption.feature.xor

import com.danyastry.encryption.core.testing.XorCipherContract

class XorCipherContractTest : XorCipherContract() {
    override val cipher = XorCipher()
}
