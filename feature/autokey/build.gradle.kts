plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.autokey"
}

dependencies {
    implementation(projects.core.classical)
}
