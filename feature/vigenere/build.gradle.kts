plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.vigenere"
}

dependencies {
    implementation(projects.core.classical)
}
