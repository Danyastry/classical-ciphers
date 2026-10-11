package com.danyastry.encryption.feature.enigma

import com.danyastry.encryption.core.testing.assertRoundTrip
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class EnigmaMachineTest {

    private val cipher = EnigmaCipher()
    private val defaults = EnigmaSettings()

    @Test
    fun `matches the classic AAAAA test`() {
        assertThat(cipher.encrypt("AAAAA", defaults)).isEqualTo("BDZGO")
    }

    @Test
    fun `decrypts the operation barbarossa message from 1941`() {
        val settings = EnigmaSettings(
            rotors = listOf(Rotor.II, Rotor.IV, Rotor.V),
            reflector = Reflector.B,
            rings = "BUL",
            positions = "BLA",
            plugboard = Plugboard.parse("AV BS CG DL FU HZ IN KM OW RX"),
        )
        val ciphertext = """
            EDPUD NRGYS ZRCXN UYTPO MRMBO FKTBZ REZKM LXLVE FGUEY SIOZV EQMIK UBPMM YLKLT TDEIS
            MDICA GYKUA CTCDO MOHWX MUUIA UBSTS LRNBZ SZWNR FXWFY SSXJZ VIJHI DISHP RKLKA YUPAD
            TXQSP INQMA TLPIF SVKDA SCTAC DPBOP VHJK
        """

        assertThat(cipher.decrypt(ciphertext, settings)).isEqualTo(
            "AUFKLXABTEILUNGXVONXKURTINOWAXKURTINOWAXNORDWESTLXSEBEZXSEBEZXUAFFLIEGERSTRASZERIQTUNGX" +
                "DUBROWKIXDUBROWKIXOPOTSCHKAXOPOTSCHKAXUMXEINSAQTDREINULLXUHRANGETRETENXANGRIFFXINFXRGTX",
        )
    }

    @Test
    fun `middle rotor double steps`() {
        val machine = EnigmaMachine(defaults.copy(positions = "ADU"))

        val windows = List(3) { machine.press('A'); machine.window }

        assertThat(windows).containsExactly("ADV", "AEW", "BFX").inOrder()
    }

    @Test
    fun `right rotor steps before every letter`() {
        val machine = EnigmaMachine(defaults)

        machine.press('A')

        assertThat(machine.window).isEqualTo("AAB")
    }

    @Test
    fun `no letter ever encrypts to itself`() {
        val machine = EnigmaMachine(defaults)

        repeat(500) { index ->
            val key = 'A' + index % 26
            assertThat(machine.press(key)).isNotEqualTo(key)
        }
    }

    @Test
    fun `the same settings decrypt what they encrypted`() {
        val settings = EnigmaSettings(
            rotors = listOf(Rotor.V, Rotor.I, Rotor.IV),
            reflector = Reflector.C,
            rings = "QWE",
            positions = "RTZ",
            plugboard = Plugboard.parse("QA WS ED RF TG"),
        )

        assertRoundTrip(cipher, "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG".repeat(5), settings)
    }

    @Test
    fun `ring settings change the output`() {
        assertThat(cipher.encrypt("AAAAA", defaults.copy(rings = "BBB"))).isNotEqualTo("BDZGO")
    }

    @Test
    fun `path goes through every component and ends at the lamp`() {
        val path = EnigmaMachine(defaults).pressWithPath('A')

        assertThat(path.keys).containsExactlyElementsIn(Stage.entries).inOrder()
        assertThat(path[Stage.KEY]).isEqualTo('A')
        assertThat(path[Stage.LAMP]).isEqualTo('B')
    }

    @Test
    fun `keeps only letters`() {
        assertThat(cipher.encrypt("a a, a!a-a", defaults)).isEqualTo("BDZGO")
    }

    @Test
    fun `rejects invalid settings`() {
        assertThrows(IllegalArgumentException::class.java) { EnigmaSettings(rotors = listOf(Rotor.I, Rotor.I, Rotor.II)) }
        assertThrows(IllegalArgumentException::class.java) { EnigmaSettings(rotors = listOf(Rotor.I, Rotor.II)) }
        assertThrows(IllegalArgumentException::class.java) { EnigmaSettings(rings = "AA") }
        assertThrows(IllegalArgumentException::class.java) { EnigmaSettings(positions = "aaa") }
        assertThrows(IllegalArgumentException::class.java) { EnigmaMachine(defaults).press('1') }
    }

    @Test
    fun `handles empty text`() {
        assertThat(cipher.encrypt("", defaults)).isEmpty()
    }
}
