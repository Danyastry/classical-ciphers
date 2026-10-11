plugins {
    alias(libs.plugins.encryption.jvm.library)
}

dependencies {
    api(projects.core.cryptoApi)
    api(libs.junit)
    api(libs.truth)
}
