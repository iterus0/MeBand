pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "MeBand"

include(
    ":common",
    ":androidApp"
)
