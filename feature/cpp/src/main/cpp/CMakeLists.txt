cmake_minimum_required(VERSION 3.22.1)

project(encryption_cpp LANGUAGES CXX)

add_library(encryption_cpp SHARED
        ciphers.cpp
        jni_bridge.cpp)

set_target_properties(encryption_cpp PROPERTIES
        CXX_VISIBILITY_PRESET hidden
        VISIBILITY_INLINES_HIDDEN ON)

target_link_options(encryption_cpp PRIVATE "-Wl,--exclude-libs,ALL")
