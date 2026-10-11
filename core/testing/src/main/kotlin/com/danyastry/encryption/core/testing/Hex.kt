package com.danyastry.encryption.core.testing

private const val HEX_DIGITS = "0123456789abcdef"

fun String.hexToBytes(): ByteArray {
    val hex = filterNot { it.isWhitespace() }
    require(hex.length % 2 == 0) { "Hex string must have an even length, was ${hex.length}" }
    return ByteArray(hex.length / 2) { index ->
        val high = hex[index * 2].hexDigit()
        val low = hex[index * 2 + 1].hexDigit()
        ((high shl 4) or low).toByte()
    }
}

fun ByteArray.toHex(): String = buildString(size * 2) {
    this@toHex.forEach { byte ->
        val value = byte.toInt() and 0xff
        append(HEX_DIGITS[value ushr 4])
        append(HEX_DIGITS[value and 0x0f])
    }
}

private fun Char.hexDigit(): Int {
    val digit = HEX_DIGITS.indexOf(lowercaseChar())
    require(digit >= 0) { "Invalid hex character '$this'" }
    return digit
}
