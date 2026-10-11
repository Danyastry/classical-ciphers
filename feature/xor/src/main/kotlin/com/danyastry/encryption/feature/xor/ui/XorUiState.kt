package com.danyastry.encryption.feature.xor.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.xor.ByteEncoding
import com.danyastry.encryption.feature.xor.XorCipher

enum class KeyFormat { TEXT, HEX }

data class XorUiState(
    val key: String = DEFAULT_KEY,
    val keyFormat: KeyFormat = KeyFormat.TEXT,
    val isKeyInvalid: Boolean = false,
    val encoding: ByteEncoding = ByteEncoding.HEX,
    val mode: CipherMode = CipherMode.ENCRYPT,
    val input: String = "",
    val isInputInvalid: Boolean = false,
    val output: String = "",
    val crib: String = "",
    val recoveredKeyText: String = "",
    val recoveredKeyHex: String = "",
) {
    companion object {
        const val DEFAULT_KEY = "ICE"
    }
}

private val cipher = XorCipher()

internal fun XorUiState.recalculated(): XorUiState {
    val keyBytes = when (keyFormat) {
        KeyFormat.TEXT -> key.encodeToByteArray()
        KeyFormat.HEX -> ByteEncoding.HEX.decodeOrNull(key)
    }?.takeIf { it.isNotEmpty() }
    val inputBytes = when (mode) {
        CipherMode.ENCRYPT -> input.encodeToByteArray()
        CipherMode.DECRYPT -> encoding.decodeOrNull(input)
    }
    if (keyBytes == null || inputBytes == null) {
        return copy(
            isKeyInvalid = keyBytes == null,
            isInputInvalid = inputBytes == null,
            output = "",
            recoveredKeyText = "",
            recoveredKeyHex = "",
        )
    }

    val result = cipher.encrypt(inputBytes, keyBytes)
    val ciphertext = if (mode == CipherMode.ENCRYPT) result else inputBytes
    val recovered = if (crib.isEmpty()) ByteArray(0) else cipher.recoverKeystream(ciphertext, crib.encodeToByteArray())
    return copy(
        isKeyInvalid = false,
        isInputInvalid = false,
        output = if (mode == CipherMode.ENCRYPT) encoding.encode(result) else result.decodeToString(),
        recoveredKeyText = recovered.decodeToString(),
        recoveredKeyHex = ByteEncoding.HEX.encode(recovered),
    )
}
