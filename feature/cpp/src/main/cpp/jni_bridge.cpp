#include <jni.h>

#include <iterator>
#include <string>
#include <vector>

#include "ciphers.h"

namespace {

constexpr const char* kNativeCiphersClass = "com/danyastry/encryption/feature/cpp/NativeCiphers";

std::u16string copyString(JNIEnv* env, jstring text) {
    const jsize length = env->GetStringLength(text);
    std::u16string buffer(static_cast<std::size_t>(length), u'\0');
    env->GetStringRegion(text, 0, length, reinterpret_cast<jchar*>(buffer.data()));
    return buffer;
}

jstring toJavaString(JNIEnv* env, const std::u16string& text) {
    return env->NewString(reinterpret_cast<const jchar*>(text.data()), static_cast<jsize>(text.size()));
}

jstring caesar(JNIEnv* env, jobject, jstring text, jint shift) {
    std::u16string buffer = copyString(env, text);
    ciphers::caesar(buffer.data(), buffer.size(), shift);
    return toJavaString(env, buffer);
}

jstring vigenere(JNIEnv* env, jobject, jstring text, jintArray shifts, jint direction) {
    const jsize shiftCount = env->GetArrayLength(shifts);
    std::vector<jint> keyShifts(static_cast<std::size_t>(shiftCount));
    env->GetIntArrayRegion(shifts, 0, shiftCount, keyShifts.data());

    std::u16string buffer = copyString(env, text);
    ciphers::vigenere(buffer.data(), buffer.size(), keyShifts.data(), keyShifts.size(), direction);
    return toJavaString(env, buffer);
}

jbyteArray xorBytes(JNIEnv* env, jobject, jbyteArray data, jbyteArray key) {
    const jsize length = env->GetArrayLength(data);
    const jsize keyLength = env->GetArrayLength(key);
    jbyteArray result = env->NewByteArray(length);
    if (result == nullptr) return nullptr;

    auto* in = static_cast<uint8_t*>(env->GetPrimitiveArrayCritical(data, nullptr));
    auto* keyBytes = static_cast<uint8_t*>(env->GetPrimitiveArrayCritical(key, nullptr));
    auto* out = static_cast<uint8_t*>(env->GetPrimitiveArrayCritical(result, nullptr));
    if (in != nullptr && keyBytes != nullptr && out != nullptr) {
        ciphers::repeatingXor(in, static_cast<std::size_t>(length), keyBytes, static_cast<std::size_t>(keyLength), out);
    }
    if (out != nullptr) env->ReleasePrimitiveArrayCritical(result, out, 0);
    if (keyBytes != nullptr) env->ReleasePrimitiveArrayCritical(key, keyBytes, JNI_ABORT);
    if (in != nullptr) env->ReleasePrimitiveArrayCritical(data, in, JNI_ABORT);
    return result;
}

const JNINativeMethod kMethods[] = {
        {"caesar", "(Ljava/lang/String;I)Ljava/lang/String;", reinterpret_cast<void*>(caesar)},
        {"vigenere", "(Ljava/lang/String;[II)Ljava/lang/String;", reinterpret_cast<void*>(vigenere)},
        {"xor", "([B[B)[B", reinterpret_cast<void*>(xorBytes)},
};

}

extern "C" JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void*) {
    JNIEnv* env = nullptr;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) return JNI_ERR;

    jclass nativeCiphers = env->FindClass(kNativeCiphersClass);
    if (nativeCiphers == nullptr) return JNI_ERR;

    const jint registered = env->RegisterNatives(nativeCiphers, kMethods, static_cast<jint>(std::size(kMethods)));
    env->DeleteLocalRef(nativeCiphers);
    return registered == JNI_OK ? JNI_VERSION_1_6 : JNI_ERR;
}
