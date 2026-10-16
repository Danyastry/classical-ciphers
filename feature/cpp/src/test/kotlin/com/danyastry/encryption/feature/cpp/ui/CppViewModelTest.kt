package com.danyastry.encryption.feature.cpp.ui

import com.danyastry.encryption.feature.cpp.BenchmarkCipher
import com.danyastry.encryption.feature.cpp.CipherBenchmark
import com.danyastry.encryption.feature.cpp.CipherSet
import com.danyastry.encryption.feature.cpp.FakeCiphers
import com.danyastry.encryption.feature.cpp.InputSize
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CppViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CppViewModel

    private val state
        get() = viewModel.state.value

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = newViewModel(cpp = FakeCiphers.reference())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `starts with caesar on 64 kb and no results`() = test {
        assertThat(state.cipher).isEqualTo(BenchmarkCipher.CAESAR)
        assertThat(state.size).isEqualTo(InputSize.MEDIUM)
        assertThat(state.comparison).isNull()
        assertThat(state.abi).isEqualTo("arm64-v8a")
    }

    @Test
    fun `comparing reports timings and matching output`() = test {
        viewModel.onCompare()
        advanceUntilIdle()

        assertThat(state.isRunning).isFalse()
        assertThat(state.comparison?.outputsMatch).isTrue()
    }

    @Test
    fun `ignores a second run while one is busy`() = test {
        viewModel.onCompare()

        assertThat(state.isRunning).isTrue()
        viewModel.onMeasureOverhead()
        advanceUntilIdle()
        assertThat(state.overhead).isNull()
        assertThat(state.isRunning).isFalse()
    }

    @Test
    fun `a broken c++ side is reported as different output`() = test {
        viewModel = newViewModel(cpp = FakeCiphers.broken())
        viewModel.onCipherChange(BenchmarkCipher.XOR)

        viewModel.onCompare()
        advanceUntilIdle()

        assertThat(state.comparison?.outputsMatch).isFalse()
    }

    @Test
    fun `changing the cipher or size clears the old result`() = test {
        viewModel.onCompare()
        advanceUntilIdle()

        viewModel.onSizeChange(InputSize.SMALL)
        assertThat(state.comparison).isNull()

        viewModel.onCompare()
        advanceUntilIdle()
        viewModel.onCipherChange(BenchmarkCipher.VIGENERE)
        assertThat(state.comparison).isNull()
        assertThat(state.cipher).isEqualTo(BenchmarkCipher.VIGENERE)
    }

    @Test
    fun `measures the call overhead separately`() = test {
        viewModel.onMeasureOverhead()
        advanceUntilIdle()

        assertThat(state.overhead?.outputsMatch).isTrue()
        assertThat(state.comparison).isNull()
    }

    private fun newViewModel(cpp: CipherSet) = CppViewModel(
        kotlinCiphers = FakeCiphers.reference(),
        cppCiphers = cpp,
        benchmark = CipherBenchmark(warmups = 0, runs = 1),
        abi = "arm64-v8a",
        dispatcher = dispatcher,
    )

    private fun test(block: suspend TestScope.() -> Unit) = runTest(dispatcher) {
        advanceUntilIdle()
        block()
    }
}
