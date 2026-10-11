package com.danyastry.encryption.feature.xor

import java.util.Base64

enum class ByteEncoding {
    HEX,
    BASE64;

    fun encode(bytes: ByteArray): String = when (this) {
        HEX -> bytes.joinToString("") { (it.toInt() and 0xff).toString(16).padStart(2, '0') }
        BASE64 -> Base64.getEncoder().encodeToString(bytes)
    }

    fun decodeOrNull(text: String): ByteArray? {
        val compact = text.filterNot { it.isWhitespace() }
        return when (this) {
            HEX -> decodeHex(compact)
            BASE64 -> runCatching { Base64.getDecoder().decode(compact) }.getOrNull()
        }
    }

    private fun decodeHex(hex: String): ByteArray? {
        if (hex.length % 2 != 0) return null
        val digits = hex.map { Character.digit(it, 16) }
        if (digits.any { it < 0 }) return null
        return ByteArray(hex.length / 2) { ((digits[it * 2] shl 4) or digits[it * 2 + 1]).toByte() }
    }
}
