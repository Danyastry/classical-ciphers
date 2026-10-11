plugins {
    alias(libs.plugins.encryption.jvm.library)
}

dependencies {
    testImplementation(projects.core.testing)
}
