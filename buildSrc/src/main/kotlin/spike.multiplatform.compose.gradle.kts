plugins {
    id("base.kotlin.compose")
    id("com.diffplug.spotless")
}

spotless {
    kotlin {
        ktfmt().googleStyle().configure {
            it.setBlockIndent(4)
            it.setContinuationIndent(4)
        }
    }
}
