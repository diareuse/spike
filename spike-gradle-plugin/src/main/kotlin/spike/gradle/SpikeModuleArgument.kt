package spike.gradle

import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.process.CommandLineArgumentProvider
import spike.Include

@Include
internal class SpikeModuleArgument(@get:Input val module: String) : CommandLineArgumentProvider {
    init {
        if (module.any { it == '=' || it.isWhitespace() }) {
            throw GradleException(
                "Spike cannot pass the module id $module to KSP, which splits processor options at '=' and " +
                    "whitespace. Rename the project."
            )
        }
    }

    override fun asArguments(): Iterable<String> = listOf("spike.module=$module")
}
