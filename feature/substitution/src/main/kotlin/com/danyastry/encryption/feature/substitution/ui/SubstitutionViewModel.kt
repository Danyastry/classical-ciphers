package com.danyastry.encryption.feature.substitution.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.classical.ALPHABET_SIZE
import com.danyastry.encryption.feature.substitution.SubstitutionKey
import java.security.SecureRandom
import kotlin.random.Random
import kotlin.random.asKotlinRandom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SubstitutionViewModel(
    private val random: Random = SecureRandom().asKotlinRandom(),
) : ViewModel() {

    private val _state = MutableStateFlow(SubstitutionUiState().recalculated())
    val state: StateFlow<SubstitutionUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onKeywordChange(keyword: String) = update {
        copy(keyword = keyword, keyText = SubstitutionKey.fromKeyword(keyword).alphabet)
    }

    fun onKeyTextChange(keyText: String) = update {
        copy(keyword = "", keyText = keyText.uppercase().filter { it in 'A'..'Z' }.take(ALPHABET_SIZE))
    }

    fun onRandomKey() = update {
        copy(keyword = "", keyText = SubstitutionKey.random(random).alphabet)
    }

    fun onModeChange(mode: SubstitutionMode) = update { copy(mode = mode) }

    fun onUseOutputAsInput() = update {
        copy(
            input = output,
            mode = if (mode == SubstitutionMode.ENCRYPT) SubstitutionMode.DECRYPT else SubstitutionMode.ENCRYPT,
        )
    }

    private fun update(transform: SubstitutionUiState.() -> SubstitutionUiState) {
        _state.update { it.transform().recalculated() }
    }
}
