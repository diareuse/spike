@dagger.Module
class ConfigModule(private val name: String) {
    @dagger.Provides
    fun name(): String = name
}
