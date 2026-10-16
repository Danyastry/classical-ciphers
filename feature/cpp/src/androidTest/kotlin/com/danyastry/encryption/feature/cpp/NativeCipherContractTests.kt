package com.danyastry.encryption.feature.cpp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.danyastry.encryption.core.testing.CaesarCipherContract
import com.danyastry.encryption.core.testing.VigenereCipherContract
import com.danyastry.encryption.core.testing.XorCipherContract
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NativeCaesarContractTest : CaesarCipherContract() {
    override val cipher = NativeCaesarCipher()
}

@RunWith(AndroidJUnit4::class)
class NativeVigenereContractTest : VigenereCipherContract() {
    override val cipher = NativeVigenereCipher()
}

@RunWith(AndroidJUnit4::class)
class NativeXorContractTest : XorCipherContract() {
    override val cipher = NativeXorCipher()
}
