package spike.preview.coffee

import spike.Include
import spike.Singleton

@Singleton
@Include
class CoffeeLogger {
    private val logs = mutableListOf<String>()
    fun log(message: String) {
        logs.add(message)
    }

    fun collect() = logs.toList()
}