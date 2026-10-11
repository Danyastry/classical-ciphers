package com.danyastry.encryption.feature.substitution

import com.danyastry.encryption.core.testing.assertRoundTrip
import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Test

class SubstitutionCipherTest {

    private val cipher = SubstitutionCipher()
    private val zebras = SubstitutionKey.fromKeyword("ZEBRAS")

    @Test
    fun `matches the reference example`() {
        assertThat(cipher.encrypt("FLEE AT ONCE. WE ARE DISCOVERED!", zebras))
            .isEqualTo("SIAA ZQ LKBA. VA ZOA RFPBLUAOAR!")
        assertThat(cipher.decrypt("SIAA ZQ LKBA. VA ZOA RFPBLUAOAR!", zebras))
            .isEqualTo("FLEE AT ONCE. WE ARE DISCOVERED!")
    }

    @Test
    fun `preserves case and leaves non letters untouched`() {
        assertThat(cipher.encrypt("Hello, World!", zebras)).isEqualTo("Daiil, Vloir!")
    }

    @Test
    fun `plain alphabet key is identity`() {
        val identity = SubstitutionKey(SubstitutionKey.PLAIN_ALPHABET)

        assertThat(cipher.encrypt("Nothing changes", identity)).isEqualTo("Nothing changes")
    }

    @Test
    fun `encrypting the plain alphabet reveals the key`() {
        assertThat(cipher.encrypt(SubstitutionKey.PLAIN_ALPHABET, zebras)).isEqualTo(zebras.alphabet)
    }

    @Test
    fun `round trips random keys`() {
        val random = Random(seed = 1)

        repeat(50) {
            assertRoundTrip(cipher, "The Quick Brown Fox Jumps Over The Lazy Dog, 123!", SubstitutionKey.random(random))
        }
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.encrypt("", zebras)).isEmpty()
        assertThat(cipher.decrypt("", zebras)).isEmpty()
    }
}
