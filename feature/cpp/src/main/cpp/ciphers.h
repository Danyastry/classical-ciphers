#pragma once

#include <cstddef>
#include <cstdint>

namespace ciphers {

void caesar(char16_t* text, std::size_t length, int shift);

void vigenere(char16_t* text, std::size_t length, const int32_t* shifts, std::size_t shiftCount, int direction);

void repeatingXor(const uint8_t* data, std::size_t length, const uint8_t* key, std::size_t keyLength, uint8_t* out);

}
