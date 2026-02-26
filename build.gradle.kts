plugins {
    // these plugins are here only because intellij wants to "commonize" on sync
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
}