package spike.gradle

import org.gradle.api.tasks.Input
import org.gradle.process.CommandLineArgumentProvider
import spike.Include

@Include
internal class SpikeModuleArgument(@get:Input val module: String) : CommandLineArgumentProvider {
    override fun asArguments(): Iterable<String> = listOf("spike.module=$module")
}
