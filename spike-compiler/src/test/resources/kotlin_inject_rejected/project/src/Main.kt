import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.Provides
import me.tatarka.inject.annotations.Qualifier

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

@Inject
fun greet(): String = "hi"
