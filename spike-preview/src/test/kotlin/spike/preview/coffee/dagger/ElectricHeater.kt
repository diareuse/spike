package spike.preview.coffee.dagger

import dagger.Binds
import javax.inject.Inject

class ElectricHeater @Inject constructor(
    private val logger: CoffeeLogger
) : Heater {
    private var heating = false
    override fun on() {
        this.heating = true
        logger.log("~ ~ ~ heating ~ ~ ~")
    }

    override fun off() {
        this.heating = false
    }

    override val isHot
        get(): Boolean {
            return heating
        }

    @dagger.Module
    interface Module {
        @Binds
        fun heater(electric: ElectricHeater): Heater
    }

}