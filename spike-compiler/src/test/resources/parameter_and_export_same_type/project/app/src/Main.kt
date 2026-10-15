@spike.EntryPoint
interface PropertyEntryPoint {
    val lib: Lib

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(lib: Lib): PropertyEntryPoint
    }

    companion object
}
