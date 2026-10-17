import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides

object Numbers {
    @Provides
    @IntoMap
    fun one(): Pair<String, Int> = "one" to 1
}

@Component
abstract class ParentComponent

@Component
abstract class ChildComponent(@Component val parent: ParentComponent) {
    @Provides
    fun name(): String = "child"
}
