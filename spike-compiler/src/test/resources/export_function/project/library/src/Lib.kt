@spike.Export
interface Exported {
    fun lib(): Lib
}

@spike.Include
class Lib {
    val name = "lib"
}
