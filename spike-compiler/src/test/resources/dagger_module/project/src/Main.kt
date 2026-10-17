import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import dagger.multibindings.StringKey
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

fun main() {
    val component = AppComponentSpike.factory().create(User("ann"))
    check(component.api.name() == "real") { "api ${component.api.name()}" }
    check(component.clock === component.clock) { "the @Singleton clock was built twice" }
    check(component.clock.zone == "UTC") { "zone ${component.clock.zone}" }
    check(component.plugins == setOf("first", "second")) { "plugins ${component.plugins}" }
    check(component.numbers == mapOf("a" to 1)) { "numbers ${component.numbers}" }
    check(component.user.name == "ann") { "user ${component.user.name}" }
    check(component.report.text == "UTC-real") { "report ${component.report.text}" }
}

interface Api {
    fun name(): String
}

class RealApi @Inject constructor() : Api {
    override fun name() = "real"
}

class Clock(val zone: String)

class User(val name: String)

class Report(val text: String)

@Module
abstract class AppModule {
    @Binds
    abstract fun api(impl: RealApi): Api

    companion object {
        @Provides
        @Singleton
        fun clock(@Named("zone") zone: String): Clock = Clock(zone)

        @Provides
        @Named("zone")
        fun zone(): String = "UTC"

        @Provides
        fun report(clock: javax.inject.Provider<Clock>, api: dagger.Lazy<Api>): Report =
            Report(clock.get().zone + "-" + api.get().name())
    }
}

@Module
object PluginModule {
    @Provides
    @IntoSet
    fun first(): String = "first"

    @Provides
    @ElementsIntoSet
    fun rest(): Set<String> = setOf("second")

    @Provides
    @IntoMap
    @StringKey("a")
    fun a(): Int = 1
}

@Singleton
@Component(modules = [AppModule::class, PluginModule::class])
interface AppComponent {
    val api: Api
    val clock: Clock
    val plugins: Set<String>
    val numbers: Map<String, Int>
    val user: User
    val report: Report

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance user: User): AppComponent
    }
}
