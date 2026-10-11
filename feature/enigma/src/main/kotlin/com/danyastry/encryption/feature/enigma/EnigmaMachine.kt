package com.danyastry.encryption.feature.enigma

import com.danyastry.encryption.core.classical.ALPHABET_SIZE
import com.danyastry.encryption.core.cryptoapi.TextCipher

data class EnigmaSettings(
    val rotors: List<Rotor> = listOf(Rotor.I, Rotor.II, Rotor.III),
    val reflector: Reflector = Reflector.B,
    val rings: String = "AAA",
    val positions: String = "AAA",
    val plugboard: Plugboard = Plugboard.EMPTY,
) {
    init {
        require(rotors.size == ROTOR_COUNT && rotors.toSet().size == ROTOR_COUNT) { "Pick three different rotors, was $rotors" }
        require(isValidSetting(rings)) { "Ring settings must be three letters A-Z, was '$rings'" }
        require(isValidSetting(positions)) { "Start positions must be three letters A-Z, was '$positions'" }
    }

    companion object {
        const val ROTOR_COUNT = 3

        fun isValidSetting(text: String): Boolean = text.length == ROTOR_COUNT && text.all { it in 'A'..'Z' }
    }
}

enum class Stage { KEY, PLUGBOARD_IN, RIGHT_IN, MIDDLE_IN, LEFT_IN, REFLECTOR, LEFT_OUT, MIDDLE_OUT, RIGHT_OUT, LAMP }

class EnigmaMachine(private val settings: EnigmaSettings) {

    private val positions = IntArray(EnigmaSettings.ROTOR_COUNT) { settings.positions[it] - 'A' }
    private val rings = IntArray(EnigmaSettings.ROTOR_COUNT) { settings.rings[it] - 'A' }

    val window: String
        get() = positions.joinToString("") { ('A' + it).toString() }

    fun press(key: Char): Char = pressWithPath(key).getValue(Stage.LAMP)

    fun pressWithPath(key: Char): Map<Stage, Char> {
        require(key in 'A'..'Z') { "Enigma keys are A-Z, was '$key'" }
        step()
        val path = linkedMapOf(Stage.KEY to key)
        var signal = settings.plugboard.swap(key - 'A').also { path[Stage.PLUGBOARD_IN] = 'A' + it }
        signal = through(RIGHT, signal, forward = true).also { path[Stage.RIGHT_IN] = 'A' + it }
        signal = through(MIDDLE, signal, forward = true).also { path[Stage.MIDDLE_IN] = 'A' + it }
        signal = through(LEFT, signal, forward = true).also { path[Stage.LEFT_IN] = 'A' + it }
        signal = settings.reflector.reflect(signal).also { path[Stage.REFLECTOR] = 'A' + it }
        signal = through(LEFT, signal, forward = false).also { path[Stage.LEFT_OUT] = 'A' + it }
        signal = through(MIDDLE, signal, forward = false).also { path[Stage.MIDDLE_OUT] = 'A' + it }
        signal = through(RIGHT, signal, forward = false).also { path[Stage.RIGHT_OUT] = 'A' + it }
        path[Stage.LAMP] = 'A' + settings.plugboard.swap(signal)
        return path
    }

    private fun step() {
        val middleAtNotch = isAtNotch(MIDDLE)
        val rightAtNotch = isAtNotch(RIGHT)
        if (middleAtNotch) {
            advance(MIDDLE)
            advance(LEFT)
        } else if (rightAtNotch) {
            advance(MIDDLE)
        }
        advance(RIGHT)
    }

    private fun isAtNotch(slot: Int): Boolean = 'A' + positions[slot] == settings.rotors[slot].notch

    private fun advance(slot: Int) {
        positions[slot] = (positions[slot] + 1) % ALPHABET_SIZE
    }

    private fun through(slot: Int, signal: Int, forward: Boolean): Int {
        val offset = positions[slot] - rings[slot]
        val rotor = settings.rotors[slot]
        return if (forward) rotor.forward(signal, offset) else rotor.backward(signal, offset)
    }

    private companion object {
        const val LEFT = 0
        const val MIDDLE = 1
        const val RIGHT = 2
    }
}

class EnigmaCipher : TextCipher<EnigmaSettings> {

    override fun encrypt(plaintext: String, key: EnigmaSettings): String {
        val machine = EnigmaMachine(key)
        return normalize(plaintext).map { machine.press(it) }.joinToString("")
    }

    override fun decrypt(ciphertext: String, key: EnigmaSettings): String = encrypt(ciphertext, key)

    companion object {
        fun normalize(text: String): String = text.uppercase().filter { it in 'A'..'Z' }
    }
}
