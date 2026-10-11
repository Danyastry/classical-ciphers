plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.polybius"
}

dependencies {
    implementation(projects.core.classical)
}
