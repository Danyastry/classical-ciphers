package com.danyastry.encryption.feature.hill

import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Assert.assertThrows
import org.junit.Test

class HillCipherTest {

    private val cipher = HillCipher()
    private val hill = HillKey.fromLetters("HILL", 2)
    private val gyb = HillKey.fromLetters("GYBNQKURP", 3)

    @Test
    fun `matches the 3x3 reference examples`() {
        assertThat(cipher.encrypt("ACT", gyb)).isEqualTo("POH")
        assertThat(cipher.encrypt("CAT", gyb)).isEqualTo("FIN")
        assertThat(cipher.decrypt("POH", gyb)).isEqualTo("ACT")
        assertThat(cipher.decrypt("FIN", gyb)).isEqualTo("CAT")
    }

    @Test
    fun `matches the 2x2 reference examples`() {
        assertThat(cipher.encrypt("SHORTEXAMPLE", hill)).isEqualTo("APADJTFTWLFJ")
        assertThat(cipher.decrypt("APADJTFTWLFJ", hill)).isEqualTo("SHORTEXAMPLE")
        assertThat(cipher.encrypt("HELP", HillKey.fromLetters("DDCF", 2))).isEqualTo("HIAT")
    }

    @Test
    fun `keeps only letters and uppercases them`() {
        assertThat(cipher.encrypt("short example!", hill)).isEqualTo("APADJTFTWLFJ")
        assertThat(cipher.decrypt("apad jtft wlfj", hill)).isEqualTo("SHORTEXAMPLE")
    }

    @Test
    fun `pads the last block with X`() {
        assertThat(cipher.encrypt("ABC", hill)).isEqualTo("ILQP")
        assertThat(cipher.decrypt("ILQP", hill)).isEqualTo("ABCX")
    }

    @Test
    fun `rejects ciphertext that is not a whole number of blocks`() {
        assertThrows(IllegalArgumentException::class.java) { cipher.decrypt("ABC", hill) }
        assertThrows(IllegalArgumentException::class.java) { cipher.decrypt("ABCD", gyb) }
    }

    @Test
    fun `round trips random keys of both sizes`() {
        val random = Random(seed = 5)
        val text = "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOGS"

        listOf(2, 3).forEach { size ->
            repeat(20) {
                val key = HillKey.fromLetters(HillKey.randomLetters(size, random), size)
                assertThat(cipher.decrypt(cipher.encrypt(text, key), key)).isEqualTo(text)
            }
        }
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.encrypt("", hill)).isEmpty()
        assertThat(cipher.decrypt("", hill)).isEmpty()
    }
}
