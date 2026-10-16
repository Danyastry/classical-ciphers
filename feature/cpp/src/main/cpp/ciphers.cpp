#include "ciphers.h"

namespace ciphers {
namespace {

constexpr int kAlphabetSize = 26;

int positiveMod(int value) {
    const int result = value % kAlphabetSize;
    return result < 0 ? result + kAlphabetSize : result;
}

template <typename Shift>
void shiftLetters(char16_t* text, std::size_t length, Shift shiftFor) {
    std::size_t letter = 0;
    for (std::size_t i = 0; i < length; ++i) {
        const char16_t c = text[i];
        char16_t base;
        if (c >= u'a' && c <= u'z') {
            base = u'a';
        } else if (c >= u'A' && c <= u'Z') {
            base = u'A';
        } else {
            continue;
        }
        const int offset = positiveMod(static_cast<int>(c - base) + shiftFor(letter++));
        text[i] = static_cast<char16_t>(base + offset);
    }
}

}

void caesar(char16_t* text, std::size_t length, int shift) {
    const int normalized = positiveMod(shift);
    shiftLetters(text, length, [normalized](std::size_t) { return normalized; });
}

void vigenere(char16_t* text, std::size_t length, const int32_t* shifts, std::size_t shiftCount, int direction) {
    shiftLetters(text, length, [=](std::size_t letter) { return direction * shifts[letter % shiftCount]; });
}

void repeatingXor(const uint8_t* data, std::size_t length, const uint8_t* key, std::size_t keyLength, uint8_t* out) {
    std::size_t i = 0;
    for (; i + keyLength <= length; i += keyLength) {
        for (std::size_t k = 0; k < keyLength; ++k) {
            out[i + k] = data[i + k] ^ key[k];
        }
    }
    for (std::size_t k = 0; i < length; ++i, ++k) {
        out[i] = data[i] ^ key[k];
    }
}

}
