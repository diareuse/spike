package spike.preview.coffee

import spike.Bind
import spike.Include

@Include
@Bind(Heater::class)
class ElectricHeater(
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
}