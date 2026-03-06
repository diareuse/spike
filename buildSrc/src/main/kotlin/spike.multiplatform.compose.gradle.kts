plugins {
    id("base.kotlin.compose")
    id("com.diffplug.spotless")
}

spotless {
    kotlin {
        ktlint().customRuleSets(
            listOf("io.nlopez.compose.rules:ktlint:0.5.6")
        )
    }
}
