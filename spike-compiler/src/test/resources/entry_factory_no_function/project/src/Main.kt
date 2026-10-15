@spike.EntryPoint
interface Configured {
    val name: String

    @spike.EntryPoint.Factory
    interface Factory

    companion object
}
