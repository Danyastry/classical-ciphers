package com.danyastry.encryption.feature.adfgvx

import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.classical.PolybiusSquare
import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Assert.assertThrows
import org.junit.Test

class AdfgvxCipherTest {

    private val cipher = AdfgvxCipher()
    private val wikipedia = AdfgvxKey(
        square = AdfgvxKey.squareFrom("NA1C3H8TB2OME5WRPD4F6G7I9J0KLQSUVXYZ"),
        transposition = Keyword("PRIVACY"),
    )

    @Test
    fun `builds the wikipedia square`() {
        assertThat(wikipedia.square.rows).containsExactly(
            "NA1C3H", "8TB2OM", "E5WRPD", "4F6G7I", "9J0KLQ", "SUVXYZ",
        ).inOrder()
    }

    @Test
    fun `fractionates every character into its row and column labels`() {
        assertThat(cipher.fractionate("attack at 1200am", wikipedia)).isEqualTo("ADDDDDADAGVGADDDAFDGVFVFADDX")
    }

    @Test
    fun `matches the wikipedia example`() {
        assertThat(cipher.encrypt("attack at 1200am", wikipedia)).isEqualTo("DGDDDAGDDGAFADDFDADVDVFAADVX")
    }

    @Test
    fun `decrypts the wikipedia example including digits`() {
        assertThat(cipher.decrypt("DGDD DAGD DGAF ADDF DADV DVFA ADVX", wikipedia)).isEqualTo("ATTACKAT1200AM")
    }

    @Test
    fun `accepts lowercase ciphertext`() {
        assertThat(cipher.decrypt("dgdddagddgafaddfdadvdvfaadvx", wikipedia)).isEqualTo("ATTACKAT1200AM")
    }

    @Test
    fun `validates ciphertext`() {
        assertThat(cipher.isValidCiphertext("AD FG")).isTrue()
        assertThat(cipher.isValidCiphertext("")).isTrue()
        assertThat(cipher.isValidCiphertext("ADF")).isFalse()
        assertThat(cipher.isValidCiphertext("ADFB")).isFalse()
        assertThat(cipher.isValidCiphertext("AD-FG")).isFalse()
    }

    @Test
    fun `rejects invalid ciphertext`() {
        assertThrows(IllegalArgumentException::class.java) { cipher.decrypt("ADF", wikipedia) }
        assertThrows(IllegalArgumentException::class.java) { cipher.decrypt("ABCD", wikipedia) }
    }

    @Test
    fun `square must be 6x6`() {
        assertThrows(IllegalArgumentException::class.java) {
            AdfgvxKey(PolybiusSquare.fromKeyword(), Keyword("KEY"))
        }
    }

    @Test
    fun `round trips random squares and keywords`() {
        val random = Random(seed = 9)
        val text = "THE1QUICK2BROWN3FOX4JUMPS5OVER6THE7LAZY8DOG9"

        listOf("A", "KEY", "PRIVACY", "CARGO", "TRANSPOSITION").forEach { keyword ->
            val key = AdfgvxKey(AdfgvxKey.squareFrom(AdfgvxKey.randomSquareKeyword(random)), Keyword(keyword))
            assertThat(cipher.decrypt(cipher.encrypt(text, key), key)).isEqualTo(text)
        }
    }

    @Test
    fun `random square keyword uses every character once`() {
        val keyword = AdfgvxKey.randomSquareKeyword(Random(seed = 1))

        assertThat(keyword.toSet()).isEqualTo(PolybiusSquare.ALPHANUMERIC_36.toSet())
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.encrypt("", wikipedia)).isEmpty()
        assertThat(cipher.decrypt("", wikipedia)).isEmpty()
    }
}
