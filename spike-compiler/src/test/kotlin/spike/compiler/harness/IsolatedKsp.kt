package spike.compiler.harness

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.ObjectOutputStream
import java.io.PrintStream
import java.lang.management.ManagementFactory
import java.lang.ref.WeakReference
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy
import java.net.URLClassLoader
import java.util.ServiceLoader
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.management.ObjectName
import kotlin.reflect.KClass

/**
 * Runs KSP the way its Gradle plugin does: KSP in one long-lived loader, and the processors in a loader that lives
 * for one run. A co-processor keeps weak references to what KSP hands it.
 */
class IsolatedKsp(private val module: SourceModule, withConverters: Boolean = true) : AutoCloseable {

    private val classpath = System.getProperty("java.class.path").split(File.pathSeparator).map(::File)
    private val ksp = URLClassLoader(
        classpath.filter(::belongsToKsp).map { it.toURI().toURL() }.toTypedArray(),
        ClassLoader.getPlatformClassLoader(),
    )
    private var processors = classpath.filter { it.isFile && !belongsToKsp(it) }
        .filter { withConverters || Converters.none { name -> name in it.name } } +
        File(System.getProperty("spike.compiler.jar"))
    private val providerType = ksp.loadClass("com.google.devtools.ksp.processing.SymbolProcessorProvider")
    private val processorType = ksp.loadClass("com.google.devtools.ksp.processing.SymbolProcessor")
    private val handedProviders = mutableListOf<String>()
    val handed = mutableListOf<WeakReference<Any>>()
    var rounds = 0
        private set

    fun addToProcessorClasspath(file: File) {
        processors = processors + file
    }

    fun addProviderJar(work: File, provider: KClass<*>, discoverable: Boolean = true) {
        val type = provider.java.name
        val directory = File("build/classes/kotlin/test").resolve(type.substringBeforeLast('.').replace('.', '/'))
        val jar = work.resolve("${provider.simpleName}-$discoverable.jar").apply { parentFile.mkdirs() }
        ZipOutputStream(jar.outputStream()).use { zip ->
            for (file in directory.listFiles().orEmpty().filter { it.name.startsWith(provider.simpleName!!) }) {
                zip.putNextEntry(ZipEntry(type.substringBeforeLast('.').replace('.', '/') + "/" + file.name))
                zip.write(file.readBytes())
                zip.closeEntry()
            }
            if (discoverable) {
                zip.putNextEntry(ZipEntry("META-INF/services/${providerType.name}"))
                zip.write(type.toByteArray())
                zip.closeEntry()
            }
        }
        addToProcessorClasspath(jar)
        if (!discoverable) handedProviders += type
    }

    fun warmLoader(): ClassLoader {
        val server = ManagementFactory.getPlatformMBeanServer()
        val name = ObjectName("spike.compiler:type=WarmLoader,version=1")
        val entry = server.invoke(name, "get", arrayOf<Any>("entry"), arrayOf("java.lang.Object")) as List<*>
        return entry[1] as ClassLoader
    }

    fun run(): String = execute().also { (code, output) -> check(code == 0) { "KSP failed: $output" } }.second

    fun runFailing(): String = execute().also { (code, output) -> check(code != 0) { "KSP succeeded: $output" } }.second

    private fun execute(): Pair<Int, String> {
        rounds = 0
        val config = ByteArrayOutputStream().also { bytes ->
            ObjectOutputStream(bytes).use { it.writeObject(module.kspConfig()) }
        }.toByteArray()
        val loader = URLClassLoader(processors.map { it.toURI().toURL() }.toTypedArray(), ksp)
        val providers = ServiceLoader.load(providerType, loader).toList() + watchingProvider() +
            handedProviders.map { loader.loadClass(it).getDeclaredConstructor().newInstance() }
        val runner = ksp.loadClass("com.google.devtools.ksp.impl.KSPLoader")
            .getMethod("loadAndRunKSP", ByteArray::class.java, List::class.java, Int::class.java)
        val output = ByteArrayOutputStream()
        val console = System.out
        val errors = System.err
        System.setOut(PrintStream(output, true))
        System.setErr(PrintStream(output, true))
        try {
            return runner.invoke(null, config, providers, InfoLevel) as Int to output.toString()
        } finally {
            System.setOut(console)
            System.setErr(errors)
            loader.close()
        }
    }

    private fun watchingProvider() = proxy(providerType) { name, arguments ->
        if (name == "create") {
            val environment = arguments.single()!!
            handed += WeakReference(environment)
            handed += WeakReference(environment.call("getLogger"))
            handed += WeakReference(environment.call("getCodeGenerator"))
            watchingProcessor()
        } else null
    }

    private fun watchingProcessor() = proxy(processorType) { name, arguments ->
        if (name == "process") {
            rounds++
            val resolver = arguments.single()!!
            handed += WeakReference(resolver)
            handed += WeakReference(resolver.call("getAllFiles").call("iterator").call("next"))
            emptyList<Any>()
        } else null
    }

    private fun proxy(type: Class<*>, answer: (String, Array<out Any?>) -> Any?): Any =
        Proxy.newProxyInstance(ksp, arrayOf(type), InvocationHandler { self, method, arguments ->
            when (method.name) {
                "hashCode" -> System.identityHashCode(self)
                "equals" -> self === arguments?.single()
                "toString" -> "IsolatedKsp watcher"
                else -> answer(method.name, arguments.orEmpty())
            }
        })

    private fun Any.call(method: String): Any = (javaClass.interfaces.toList() + javaClass)
        .firstNotNullOf { type -> type.methods.firstOrNull { it.name == method && it.parameterCount == 0 } }
        .invoke(this)

    private fun belongsToKsp(file: File) = KspJars.any { it in file.name }

    override fun close() = ksp.close()

    private companion object {
        const val InfoLevel = 1
        val KspJars = listOf("symbol-processing", "kotlin-stdlib", "kotlinx-coroutines", "annotations-")
        val Converters = listOf("dagger", "jsr", "koin", "metro", "kotlin-inject")
    }
}
