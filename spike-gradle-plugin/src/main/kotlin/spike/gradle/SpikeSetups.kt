package spike.gradle

import spike.BindTarget
import spike.EntryPoint
import spike.Include
import spike.Key

@EntryPoint
internal interface SpikeSetups {
    val setup: SpikeSetup

    companion object
}

@Key
@Retention(AnnotationRetention.SOURCE)
internal annotation class Order(val value: Int)

@Include
internal fun artifacts(version: SpikeVersionResource): SpikeArtifacts = SpikeArtifacts(version.value())

@Include
internal fun setup(setups: Map<Int, SpikeSetup>): SpikeSetup = SpikeSetupAll(setups.toSortedMap().values.toList())

@Include(bindTo = BindTarget.Map)
@Order(10)
internal fun ksp(): SpikeSetup = SpikeSetupKsp()

@Include(bindTo = BindTarget.Map)
@Order(20)
internal fun multiplatform(artifacts: SpikeArtifacts): SpikeSetup = SpikeSetupMultiplatform(artifacts)

@Include(bindTo = BindTarget.Map)
@Order(30)
internal fun jvm(artifacts: SpikeArtifacts): SpikeSetup =
    SpikeSetupSinglePlatform("org.jetbrains.kotlin.jvm", artifacts)

@Include(bindTo = BindTarget.Map)
@Order(40)
internal fun android(artifacts: SpikeArtifacts): SpikeSetup =
    SpikeSetupSinglePlatform("org.jetbrains.kotlin.android", artifacts)
