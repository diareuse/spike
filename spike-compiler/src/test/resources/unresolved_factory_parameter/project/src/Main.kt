@spike.EntryPoint
interface App {
    val screen: Screen

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(context: Context): App
    }

    companion object
}

@spike.Include
class Screen(val context: Context)
