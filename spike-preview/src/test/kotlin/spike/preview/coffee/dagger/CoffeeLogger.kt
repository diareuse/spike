package spike.preview.coffee.dagger

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoffeeLogger @Inject constructor() {
    private val logs = mutableListOf<String>()
    fun log(message: String) {
        logs.add(message)
    }

    fun collect() = logs.toList()
}