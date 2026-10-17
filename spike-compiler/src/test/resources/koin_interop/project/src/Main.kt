import org.koin.core.Koin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import spike.koin.FromKoin

fun main() {
    val koin = koinApplication { modules(module { single { Clock() } }) }.koin
    val app = App.factory().create(koin)
    check(app.repo.clock === koin.get<Clock>()) { "Spike did not take Clock from Koin" }

    val consumer = koinApplication { modules(app.koinModule()) }.koin
    check(consumer.get<Repo>().clock === koin.get<Clock>()) { "Koin did not get Repo from Spike" }
}

class Clock

@spike.Include
class Repo(val clock: Clock)

@FromKoin(Clock::class)
@spike.EntryPoint
interface App {
    val repo: Repo

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(koin: Koin): App
    }

    companion object
}
