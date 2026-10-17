import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.StringKey

fun main() {
    val graph = AppGraphSpike.factory().create(User("ann"))
    check(graph.repo.api.name() == "real") { "api ${graph.repo.api.name()}" }
    check(graph.repo.clock === graph.clock) { "the @SingleIn clock was built twice" }
    check(graph.repo.url == "https://example.com") { "url ${graph.repo.url}" }
    check(graph.plugins == setOf("plugin")) { "plugins ${graph.plugins}" }
    check(graph.numbers == mapOf("one" to 1)) { "numbers ${graph.numbers}" }
    check(graph.details.create("42").id == "42") { "detail ${graph.details.create("42").id}" }
    check(graph.logger.tag == "log") { "logger ${graph.logger.tag}" }
    check(graph.user.name == "ann") { "user ${graph.user.name}" }
}

interface Api {
    fun name(): String
}

@ContributesBinding(AppScope::class)
@Inject
class RealApi : Api {
    override fun name() = "real"
}

interface Logger {
    val tag: String
}

@Inject
class TagLogger : Logger {
    override val tag = "log"
}

class User(val name: String)

@SingleIn(AppScope::class)
@Inject
class Clock

@Inject
class Repo(val api: Api, val clock: Clock, @Named("url") val url: String)

@AssistedInject
class Detail(@Assisted val id: String, val repo: Repo) {
    @AssistedFactory
    fun interface Factory {
        fun create(id: String): Detail
    }
}

@ContributesTo(AppScope::class)
interface NetworkBindings {
    @Binds
    val TagLogger.bind: Logger

    companion object {
        @Provides
        @Named("url")
        fun url(): String = "https://example.com"

        @Provides
        @IntoSet
        fun plugin(): String = "plugin"

        @Provides
        @IntoMap
        @StringKey("one")
        fun one(): Int = 1
    }
}

@DependencyGraph(AppScope::class)
interface AppGraph {
    val repo: Repo
    val clock: Clock
    val plugins: Set<String>
    val numbers: Map<String, Int>
    val details: Detail.Factory
    val logger: Logger
    val user: User

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides user: User): AppGraph
    }
}
