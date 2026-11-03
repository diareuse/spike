package spike.gradle

import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.process.CommandLineArgumentProvider
import spike.Include

@Include
internal class SpikeLibraryArgument(@get:Input val library: Provider<Boolean>) : CommandLineArgumentProvider {
    override fun asArguments(): Iterable<String> =
        if (library.get()) listOf("spike.library=true") else emptyList()
}
