import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.GraphExtension
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.binding

interface Api

interface Other

@ContributesBinding(AppScope::class, binding = binding<Api>())
@Inject
class RealApi : Api, Other

@GraphExtension
interface ScreenGraph

@DependencyGraph(AppScope::class)
interface AppGraph {
    @Provides
    fun name(): String = "app"
}

@Inject
class Lazily(val api: dev.zacsweers.metro.Provider<Api>)

object Defaults {
    @Provides
    @dev.zacsweers.metro.OptionalBinding
    fun name(): String? = null
}
