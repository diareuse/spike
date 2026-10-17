import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import me.tatarka.inject.annotations.Scope

fun main() {
    val component = AppComponentSpike.factory().create(User("ann"))
    check(component.repo.api.name() == "real") { "api ${component.repo.api.name()}" }
    check(component.repo.config.url == "https://example.com") { "config ${component.repo.config.url}" }
    check(component.clock === component.repo.clock) { "the scoped clock was built twice" }
    check(component.plugins == setOf("plugin")) { "plugins ${component.plugins}" }
    check(component.detail("42").id == "42") { "detail ${component.detail("42").id}" }
    check(component.user.name == "ann") { "user ${component.user.name}" }
}

@Scope
annotation class AppScope

interface Api {
    fun name(): String
}

@Inject
class RealApi : Api {
    override fun name() = "real"
}

@AppScope
@Inject
class Clock

class Config(val url: String)

class User(val name: String)

@Inject
class Repo(val api: Api, val clock: Clock, val config: Config)

@Inject
class Detail(@Assisted val id: String, val repo: Repo)

interface NetworkComponent {
    @Provides
    fun RealApi.bind(): Api = this

    @Provides
    fun config(): Config = Config("https://example.com")

    @Provides
    @IntoSet
    fun plugin(): String = "plugin"
}

@AppScope
@Component
abstract class AppComponent(@get:Provides val user: User) : NetworkComponent {
    abstract val repo: Repo
    abstract val clock: Clock
    abstract val plugins: Set<String>
    abstract val detail: (String) -> Detail
}
