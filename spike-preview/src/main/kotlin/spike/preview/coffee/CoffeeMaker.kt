package spike.preview.coffee

import spike.Include

@Include
class CoffeeMaker(
    private val logger: CoffeeLogger,
    private val heater: Heater,
    private val pump: Pump
) {

    fun brew() {
        heater.on()
        pump.pump()
        logger.log(" [_]P coffee! [_]P ")
        heater.off()
    }
}