plugins {
    alias(libs.plugins.encryption.android.library.compose)
}

android {
    namespace = "com.danyastry.encryption.core.ui"
}

dependencies {
    api(projects.core.cryptoApi)
    api(libs.androidx.material3)
}
