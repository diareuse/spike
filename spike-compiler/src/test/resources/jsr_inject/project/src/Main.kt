import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

fun main() {
    val app = App()
    val repo = app.settings.repo
    check(repo.url == "https://example.com") { "url ${repo.url}" }
    check(repo.clock.get() === app.clock) { "the javax @Singleton clock was built twice" }
    check(app.settings !== app.settings) { "an unscoped @Inject class must not be cached" }
}

class Api @Inject constructor()

@Singleton
class Clock @Inject constructor()

class Repo @Inject constructor(
    val api: Api,
    @Named("base") val url: String,
    val clock: javax.inject.Provider<Clock>,
)

class Settings @jakarta.inject.Inject constructor(val repo: Repo)

@spike.Include
@spike.jsr.Named("base")
fun baseUrl(): String = "https://example.com"

@spike.EntryPoint
interface App {
    val settings: Settings
    val clock: Clock

    companion object
}
