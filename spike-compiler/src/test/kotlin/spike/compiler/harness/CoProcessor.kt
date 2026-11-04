package spike.compiler.harness

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import spike.compiler.conversion.loading.SymbolProcessorProviderLoading
import java.io.File
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream

class CoProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        if (environment.options[CoProcessorJar.StashOption].toBoolean()) CoProcessorStash.environment = environment
        check(!environment.options[CoProcessorJar.CreateCrashOption].toBoolean()) { "The co-processor crashed" }
        return CoProcessor(environment.options[CoProcessorJar.CrashOption].toBoolean())
    }
}

object CoProcessorStash {
    var environment: SymbolProcessorEnvironment? = null
}

class CoProcessor(private val crashes: Boolean) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        check(!crashes) { "The co-processor crashed" }
        return emptyList()
    }
}

class LeasedProcessorProvider : SymbolProcessorProviderLoading(LeasedProcessorImplementation::class.java)

class LeasedProcessorImplementation : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        check(!environment.options[CoProcessorJar.LeasedCreateCrashOption].toBoolean()) { "The co-processor crashed" }
        return CoProcessor(environment.options[CoProcessorJar.LeasedCrashOption].toBoolean())
    }
}

object CoProcessorJar {
    const val CrashOption = "co.processor.crash"
    const val CreateCrashOption = "co.processor.crash.create"
    const val LeasedCrashOption = "co.processor.leased.crash"
    const val LeasedCreateCrashOption = "co.processor.leased.crash.create"
    const val StashOption = "co.processor.stash"
    val ProviderName: String = CoProcessorProvider::class.java.name

    fun writeTo(file: File) = file.apply {
        parentFile.mkdirs()
        JarOutputStream(outputStream()).use { jar ->
            val providers = listOf(ProviderName, LeasedProcessorProvider::class.java.name).joinToString("\n")
            jar.add("META-INF/services/${SymbolProcessorProvider::class.java.name}", providers.toByteArray())
            listOf(
                CoProcessorProvider::class.java,
                CoProcessorStash::class.java,
                CoProcessor::class.java,
                LeasedProcessorProvider::class.java,
                LeasedProcessorImplementation::class.java,
            ).forEach { type ->
                val path = type.name.replace('.', '/') + ".class"
                jar.add(path, type.classLoader.getResourceAsStream(path)!!.use { it.readBytes() })
            }
        }
    }

    private fun JarOutputStream.add(name: String, content: ByteArray) {
        putNextEntry(JarEntry(name))
        write(content)
        closeEntry()
    }
}
