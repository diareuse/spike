plugins {
    id("base.kotlin.jvm")
    id("com.diffplug.spotless")
}

spotless {
    kotlin {
        targetExclude("**/test/**", "**/build/**", "**/commonTest/**")
        ktfmt().googleStyle().configure {
            it.setBlockIndent(4)
            it.setContinuationIndent(4)
        }
    }
}
