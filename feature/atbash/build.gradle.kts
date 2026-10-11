plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.atbash"
}

dependencies {
    implementation(projects.core.classical)
}
