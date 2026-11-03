@spike.Export
interface Exported {
    fun lib(): Lib
    fun settings(): Settings
}

@spike.Include
class Lib
