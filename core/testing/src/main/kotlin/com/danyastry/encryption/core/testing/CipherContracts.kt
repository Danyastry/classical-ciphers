package com.danyastry.encryption.core.testing

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.cryptoapi.ByteCipher
import com.danyastry.encryption.core.cryptoapi.TextCipher
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

private const val MESSAGE = "Meet me at the old bridge at 9:45 PM. Bring the map, not the phone!\n"

fun longMessage(): String = MESSAGE.repeat(100)

abstract class CaesarCipherContract {

    abstract val cipher: TextCipher<Int>

    @Test
    fun encryptsWithTheClassicShiftOfThree() {
        assertThat(cipher.encrypt("HELLO", 3)).isEqualTo("KHOOR")
    }

    @Test
    fun matchesTheReferencePangramExample() {
        assertThat(cipher.encrypt("THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG", 23))
            .isEqualTo("QEB NRFZH YOLTK CLU GRJMP LSBO QEB IXWV ALD")
    }

    @Test
    fun wrapsAroundAndPreservesCase() {
        assertThat(cipher.encrypt("xyz XYZ Hello", 3)).isEqualTo("abc ABC Khoor")
    }

    @Test
    fun leavesDigitsAndPunctuationUntouched() {
        assertThat(cipher.encrypt("Meet me at 9:45, gate #12!", 1)).isEqualTo("Nffu nf bu 9:45, hbuf #12!")
    }

    @Test
    fun normalizesLargeAndNegativeShifts() {
        assertThat(cipher.encrypt("abc", 29)).isEqualTo("def")
        assertThat(cipher.encrypt("abc", -3)).isEqualTo("xyz")
        assertThat(cipher.encrypt("abc", Int.MIN_VALUE)).isEqualTo("cde")
    }

    @Test
    fun emptyTextStaysEmpty() {
        assertThat(cipher.encrypt("", 5)).isEmpty()
        assertThat(cipher.decrypt("", 5)).isEmpty()
    }

    @Test
    fun decryptsALongMessageBackForEveryShift() {
        val message = longMessage()

        for (shift in -30..30) assertRoundTrip(cipher, message, shift)
    }
}

abstract class VigenereCipherContract {

    abstract val cipher: TextCipher<Keyword>

    private val lemon = Keyword("LEMON")

    @Test
    fun matchesTheTextbookAttackAtDawnExample() {
        assertThat(cipher.encrypt("ATTACKATDAWN", lemon)).isEqualTo("LXFOPVEFRNHR")
        assertThat(cipher.decrypt("LXFOPVEFRNHR", lemon)).isEqualTo("ATTACKATDAWN")
    }

    @Test
    fun onlyLettersMoveTheKeywordForward() {
        assertThat(cipher.encrypt("Attack at dawn!", lemon)).isEqualTo("Lxfopv ef rnhr!")
    }

    @Test
    fun aSingleLetterKeywordIsACaesarShift() {
        assertThat(cipher.encrypt("Hello", Keyword("D"))).isEqualTo("Khoor")
    }

    @Test
    fun leavesDigitsAndPunctuationUntouched() {
        assertThat(cipher.encrypt("Meet at 9:45!", lemon)).isEqualTo("Xiqh ne 9:45!")
    }

    @Test
    fun emptyTextStaysEmpty() {
        assertThat(cipher.encrypt("", lemon)).isEmpty()
    }

    @Test
    fun decryptsALongMessageBack() {
        val message = longMessage()

        listOf("A", "LEMON", "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG").forEach { assertRoundTrip(cipher, message, Keyword(it)) }
    }
}

abstract class XorCipherContract {

    abstract val cipher: ByteCipher<ByteArray>

    @Test
    fun repeatsTheKeyOverTheData() {
        val data = byteArrayOf(0x00, 0x0F, 0xF0.toByte(), 0xFF.toByte(), 0x55)
        val key = byteArrayOf(0x0F, 0xF0.toByte())

        assertThat(cipher.encrypt(data, key)).isEqualTo(byteArrayOf(0x0F, 0xFF.toByte(), 0xFF.toByte(), 0x0F, 0x5A))
    }

    @Test
    fun encryptAndDecryptAreTheSameOperation() {
        val data = "Attack at dawn".encodeToByteArray()
        val key = "KEY".encodeToByteArray()

        assertThat(cipher.decrypt(data, key)).isEqualTo(cipher.encrypt(data, key))
    }

    @Test
    fun aKeyOfZerosChangesNothing() {
        val data = "Attack at dawn".encodeToByteArray()

        assertThat(cipher.encrypt(data, ByteArray(3))).isEqualTo(data)
    }

    @Test
    fun emptyDataStaysEmpty() {
        assertThat(cipher.encrypt(ByteArray(0), byteArrayOf(1))).isEmpty()
    }

    @Test
    fun rejectsAnEmptyKey() {
        assertThrows(IllegalArgumentException::class.java) { cipher.encrypt(byteArrayOf(1, 2), ByteArray(0)) }
    }

    @Test
    fun worksForEveryPossibleByteValue() {
        val allBytes = ByteArray(256) { it.toByte() }

        assertRoundTrip(cipher, allBytes, "secret".encodeToByteArray())
    }

    @Test
    fun decryptsALongMessageBackWithKeysOfAnyLength() {
        val message = longMessage().encodeToByteArray()

        listOf("K", "secret", "a key that is longer than sixteen bytes").forEach { key ->
            assertRoundTrip(cipher, message, key.encodeToByteArray())
        }
    }
}
