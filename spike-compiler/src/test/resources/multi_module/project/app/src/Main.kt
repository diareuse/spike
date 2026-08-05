fun main() {
    val ep: PropertyEntryPoint = PropertyEntryPoint.invoke()
    check(ep.lib.run() == "library")
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val lib: Lib

    companion object
}

@spike.Include
fun prefix() = "lib"