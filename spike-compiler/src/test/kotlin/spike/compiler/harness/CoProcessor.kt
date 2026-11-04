package spike.compiler.harness

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import java.io.File
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream

class CoProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) =
        CoProcessor(environment.options[CoProcessorJar.CrashOption].toBoolean())
}

class CoProcessor(private val crashes: Boolean) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        check(!crashes) { "The co-processor crashed" }
        return emptyList()
    }
}

object CoProcessorJar {
    const val CrashOption = "co.processor.crash"
    val ProviderName: String = CoProcessorProvider::class.java.name

    fun writeTo(file: File) = file.apply {
        parentFile.mkdirs()
        JarOutputStream(outputStream()).use { jar ->
            jar.add("META-INF/services/${SymbolProcessorProvider::class.java.name}", ProviderName.toByteArray())
            listOf(CoProcessorProvider::class.java, CoProcessor::class.java).forEach { type ->
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
