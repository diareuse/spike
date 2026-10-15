@spike.Export
interface Exported {
    val lib: Lib
    fun otherLib(): Lib
}

@spike.Include
class Lib
