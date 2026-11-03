package spike.gradle

import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

internal fun KotlinTarget.usesAndroidSourceSets() =
    platformType == KotlinPlatformType.androidJvm && this is KotlinAndroidTarget
