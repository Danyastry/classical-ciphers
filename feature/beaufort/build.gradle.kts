plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.beaufort"
}

dependencies {
    implementation(projects.core.classical)
}
