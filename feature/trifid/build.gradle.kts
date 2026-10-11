plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.trifid"
}

dependencies {
    implementation(projects.core.classical)
}
