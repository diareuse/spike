@spike.EntryPoint
interface PropertyEntryPoint {
    val lib: Lib

    companion object
}

@spike.Include
fun prefix() = "lib"

@spike.Include
fun otherLib(): Lib = Lib("other")
