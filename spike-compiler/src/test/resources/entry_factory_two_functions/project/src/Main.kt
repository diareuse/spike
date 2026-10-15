@spike.EntryPoint
interface Configured {
    val name: String

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(name: String): Configured
        fun createDefault(): Configured
    }

    companion object
}
