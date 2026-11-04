package spike.compiler.conversion.loading

import spike.Include

fun interface HostSystem {
    fun locksOpenFiles(): Boolean
}

class HostSystemNamed(private val name: String) : HostSystem {
    override fun locksOpenFiles() = name.startsWith("Windows")
}

@Include
fun hostSystem(): HostSystem = HostSystemNamed(System.getProperty("os.name"))
