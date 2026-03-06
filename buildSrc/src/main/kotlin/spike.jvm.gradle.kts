plugins {
    id("base.kotlin.jvm")
    id("com.diffplug.spotless")
}

spotless {
    kotlin {
        targetExclude("**/test/**", "**/build/**", "**/commonTest/**")
        ktlint()
    }
}
