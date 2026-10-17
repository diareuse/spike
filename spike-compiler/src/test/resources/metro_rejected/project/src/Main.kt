import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.Qualifier

class Hidden @Inject private constructor()

@Inject
class Box<T>

@Qualifier
annotation class Remote

@Inject
class Api(@Remote val url: String)

interface Named {
    val name: String

    @Provides
    fun greeting(): String = "hi $name"
}

@AssistedFactory
interface Empty

class Settings(val url: String)

@DependencyGraph(AppScope::class)
interface AppGraph {
    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Includes settings: Settings): AppGraph
    }
}
