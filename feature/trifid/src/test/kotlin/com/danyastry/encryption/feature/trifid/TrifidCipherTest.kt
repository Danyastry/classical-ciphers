package com.danyastry.encryption.feature.trifid

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class TrifidCipherTest {

    private val cipher = TrifidCipher()
    private val wikipedia = TrifidKey(TrifidCube.fromKeyword("FELIX MARIE DELASTELLE"), period = 5)

    @Test
    fun `matches the wikipedia example`() {
        assertThat(cipher.encrypt("Aide-toi, le ciel t'aidera", wikipedia)).isEqualTo("FMJFVOISSUFTFPUFEQQC")
        assertThat(cipher.decrypt("FMJFVOISSUFTFPUFEQQC", wikipedia)).isEqualTo("AIDETOILECIELTAIDERA")
    }

    @Test
    fun `matches the practical cryptography example`() {
        val key = TrifidKey(TrifidCube.fromKeyword("EPSDUCVWYM+ZLKXNBTFGORIJHAQ"), period = 5)

        assertThat(cipher.encrypt("defend the east wall of the castle", key)).isEqualTo("SUEFECPHSEGYYJIXIMFOFOCEJLRF")
    }

    @Test
    fun `period changes the result and one changes nothing`() {
        val text = "AIDETOILECIELTAIDERA"

        assertThat(cipher.encrypt(text, wikipedia.copy(period = 0))).isNotEqualTo(cipher.encrypt(text, wikipedia))
        assertThat(cipher.encrypt(text, wikipedia.copy(period = 1))).isEqualTo(text)
    }

    @Test
    fun `keeps J as its own letter`() {
        assertThat(cipher.decrypt(cipher.encrypt("JAZZ+JAM", wikipedia), wikipedia)).isEqualTo("JAZZ+JAM")
    }

    @Test
    fun `round trips different periods and lengths`() {
        val text = "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG+"

        (0..12).forEach { period ->
            (0..text.length).forEach { length ->
                val key = wikipedia.copy(period = period)
                assertThat(cipher.decrypt(cipher.encrypt(text.take(length), key), key)).isEqualTo(text.take(length))
            }
        }
    }

    @Test
    fun `rejects negative periods`() {
        assertThrows(IllegalArgumentException::class.java) { wikipedia.copy(period = -1) }
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.encrypt("", wikipedia)).isEmpty()
        assertThat(cipher.decrypt("", wikipedia)).isEmpty()
    }
}
