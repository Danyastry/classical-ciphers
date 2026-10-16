package com.danyastry.encryption.feature.cpp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import java.lang.reflect.Modifier
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NativeRegistrationTest {

    private val externalFunctions = NativeCiphers::class.java.declaredMethods.filter { Modifier.isNative(it.modifiers) }

    @Test
    fun findsTheExternalFunctions() {
        assertThat(externalFunctions).isNotEmpty()
    }

    @Test
    fun everyExternalFunctionIsRegisteredInJniOnLoad() {
        externalFunctions.forEach { function ->
            val arguments = function.parameterTypes.map(::sampleArgument).toTypedArray()

            assertThat(function.invoke(NativeCiphers, *arguments)).isNotNull()
        }
    }

    private fun sampleArgument(type: Class<*>): Any = when (type) {
        String::class.java -> "abc"
        Int::class.javaPrimitiveType -> 1
        IntArray::class.java -> intArrayOf(1)
        ByteArray::class.java -> byteArrayOf(1)
        else -> error("Add a sample value for ${type.name}")
    }
}
