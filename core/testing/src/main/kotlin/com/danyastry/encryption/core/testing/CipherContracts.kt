package com.danyastry.encryption.core.testing

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.cryptoapi.ByteCipher
import com.danyastry.encryption.core.cryptoapi.TextCipher
import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Assert.assertThrows
import org.junit.Test

private const val MIXED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 .,!?-\n\tÄöüßéЖ€✓"

fun randomMixedText(length: Int, random: Random = Random(42)): String =
    buildString(length) { repeat(length) { append(MIXED_CHARACTERS[random.nextInt(MIXED_CHARACTERS.length)]) } } + "😀"

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
    fun leavesEverythingExceptAsciiLettersUntouched() {
        assertThat(cipher.encrypt("a1, Ä ж € ✓ 😀 z", 1)).isEqualTo("b1, Ä ж € ✓ 😀 a")
    }

    @Test
    fun normalizesLargeAndNegativeShifts() {
        assertThat(cipher.encrypt("abc", 29)).isEqualTo("def")
        assertThat(cipher.encrypt("abc", -3)).isEqualTo("xyz")
        assertThat(cipher.encrypt("abc", Int.MIN_VALUE)).isEqualTo(cipher.encrypt("abc", Int.MIN_VALUE.mod(26)))
    }

    @Test
    fun emptyTextStaysEmpty() {
        assertThat(cipher.encrypt("", 5)).isEmpty()
        assertThat(cipher.decrypt("", 5)).isEmpty()
    }

    @Test
    fun roundTripsLongMixedTextForEveryShift() {
        val text = randomMixedText(5_000)

        for (shift in -30..30) assertRoundTrip(cipher, text, shift)
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
    fun leavesEverythingExceptAsciiLettersUntouched() {
        assertThat(cipher.encrypt("ж€ 😀", lemon)).isEqualTo("ж€ 😀")
    }

    @Test
    fun emptyTextStaysEmpty() {
        assertThat(cipher.encrypt("", lemon)).isEmpty()
    }

    @Test
    fun roundTripsLongMixedText() {
        val text = randomMixedText(5_000)

        listOf("A", "LEMON", "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG").forEach { assertRoundTrip(cipher, text, Keyword(it)) }
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
    fun xorWithZerosChangesNothing() {
        val data = Random(7).nextBytes(64)

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
    fun roundTripsLongRandomData() {
        val random = Random(42)
        val data = random.nextBytes(100_000)

        listOf(1, 7, 32, 1_000).forEach { assertRoundTrip(cipher, data, random.nextBytes(it)) }
    }
}
