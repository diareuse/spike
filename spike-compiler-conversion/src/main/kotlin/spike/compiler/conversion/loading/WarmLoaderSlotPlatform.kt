package spike.compiler.conversion.loading

import spike.Include
import java.lang.management.ManagementFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiFunction
import javax.management.InstanceAlreadyExistsException
import javax.management.MBeanServer
import javax.management.ObjectName
import javax.management.StandardMBean

/**
 * Keeps the entry in the platform MBean server: Gradle restores system properties for each build, and a static field
 * would die with the per-run loader that holds it.
 */
@Include
class WarmLoaderSlotPlatform : WarmLoaderSlot {
    override fun update(replace: (WarmLoaderEntry?) -> WarmLoaderEntry?): WarmLoaderEntry? {
        val server = ManagementFactory.getPlatformMBeanServer()
        register(server)
        val update = BiFunction<Any, Any?, Any?> { _, stored -> replace(WarmLoaderEntry.ofStored(stored))?.toStored() }
        return WarmLoaderEntry.ofStored(server.invoke(Name, "compute", arrayOf(Key, update), ComputeSignature))
    }

    @Suppress("UNCHECKED_CAST")
    private fun register(server: MBeanServer) {
        if (server.isRegistered(Name)) return
        try {
            server.registerMBean(StandardMBean(ConcurrentHashMap<Any, Any>(), Map::class.java as Class<Map<Any, Any>>), Name)
        } catch (_: InstanceAlreadyExistsException) {
            return
        }
    }

    private companion object {
        val Name = ObjectName("spike.compiler:type=WarmLoader,version=1")
        const val Key = "entry"
        val ComputeSignature = arrayOf("java.lang.Object", "java.util.function.BiFunction")
    }
}
