plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.hill"
}

dependencies {
    implementation(projects.core.classical)
}
