package spike.preview.coffee

import spike.Bind
import spike.Include

@Include
@Bind(Pump::class)
class Thermosiphon(
    private val logger: CoffeeLogger,
    private val heater: Heater
) : Pump {
    override fun pump() {
        if (heater.isHot) {
            logger.log("=> => pumping => =>")
        }
    }
}