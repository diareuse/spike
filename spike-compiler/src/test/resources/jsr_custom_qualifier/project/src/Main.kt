import javax.inject.Inject
import javax.inject.Qualifier

@Qualifier
annotation class Remote

class Url

class Api @Inject constructor(@Remote val url: Url)
