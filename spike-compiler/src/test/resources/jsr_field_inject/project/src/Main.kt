import javax.inject.Inject

class Api @Inject constructor()

class Screen {
    @Inject
    lateinit var api: Api
}
