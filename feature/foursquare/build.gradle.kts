plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.foursquare"
}

dependencies {
    implementation(projects.core.classical)
}
