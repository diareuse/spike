fun main() {
    val ep: PropertyEntryPoint = PropertyEntryPoint.invoke()
    check(ep.lib.run() == "rary")
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val lib: Lib

    companion object
}
