@spike.Export
interface Exported {
    val lib: Lib
}

@spike.Include
class Lib {
    fun run() = "rary"
}