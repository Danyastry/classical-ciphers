pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "encryptionN1"

include(":app")
include(":core:classical")
include(":core:crypto-api")
include(":core:testing")
include(":core:ui")

include(":feature:caesar")
include(":feature:rot")
include(":feature:atbash")
include(":feature:affine")
include(":feature:substitution")
include(":feature:vigenere")
include(":feature:beaufort")
include(":feature:autokey")
include(":feature:playfair")
include(":feature:hill")
include(":feature:railfence")
include(":feature:columnar")
include(":feature:scytale")
include(":feature:bacon")
include(":feature:polybius")
include(":feature:adfgvx")
include(":feature:bifid")
include(":feature:trifid")
include(":feature:foursquare")
include(":feature:twosquare")
include(":feature:enigma")
include(":feature:onetimepad")
include(":feature:xor")
include(":feature:cpp")
