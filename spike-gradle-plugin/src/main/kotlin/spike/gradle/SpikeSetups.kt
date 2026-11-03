package spike.gradle

import org.gradle.api.provider.Provider
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
@Order(14)
internal fun extension(): SpikeSetup = SpikeSetupExtension()

@Include(bindTo = BindTarget.Map)
@Order(15)
internal fun libraryRole(
    tasks: KspTasks,
    argument: (Provider<Boolean>) -> SpikeLibraryArgument,
): SpikeSetup = SpikeSetupLibraryRole(tasks, argument)

@Include(bindTo = BindTarget.Map)
@Order(16)
internal fun moduleId(tasks: KspTasks, argument: (String) -> SpikeModuleArgument): SpikeSetup =
    SpikeSetupModuleId(tasks, argument)

@Include(bindTo = BindTarget.Map)
@Order(20)
internal fun multiplatform(artifacts: SpikeArtifacts, configurations: KspConfigurations): SpikeSetup =
    SpikeSetupMultiplatform(artifacts, configurations)

@Include(bindTo = BindTarget.Map)
@Order(30)
internal fun jvm(artifacts: SpikeArtifacts, configurations: KspConfigurations): SpikeSetup =
    SpikeSetupSinglePlatform("org.jetbrains.kotlin.jvm", artifacts, configurations)

@Include(bindTo = BindTarget.Map)
@Order(40)
internal fun android(artifacts: SpikeArtifacts, configurations: KspConfigurations): SpikeSetup =
    SpikeSetupSinglePlatform("org.jetbrains.kotlin.android", artifacts, configurations)

@Include
internal fun kspConfigurations(configurations: Map<Int, KspConfigurations>): KspConfigurations =
    KspConfigurationsAll(configurations.toSortedMap().values.toList())

@Include(bindTo = BindTarget.Map)
@Order(10)
internal fun kotlinConfigurations(configurations: KspConfigurationsOfKotlinCompilations): KspConfigurations =
    configurations

@Include(bindTo = BindTarget.Map)
@Order(20)
internal fun androidConfigurations(configurations: KspConfigurationsOfAndroidSourceSets): KspConfigurations =
    configurations
