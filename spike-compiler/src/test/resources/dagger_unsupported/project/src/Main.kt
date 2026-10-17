class Api

@dagger.Module
object PrivateModule {
    @dagger.Provides
    private fun api(): Api = Api()
}

@dagger.Module
object ReceiverModule {
    @dagger.Provides
    fun Api.name(): String = "api"
}

@dagger.Module
object KeylessModule {
    @dagger.Provides
    @dagger.multibindings.IntoMap
    fun one(): Int = 1
}
