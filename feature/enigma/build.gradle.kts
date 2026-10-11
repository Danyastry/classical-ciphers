plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.enigma"
}

dependencies {
    implementation(projects.core.classical)
}
