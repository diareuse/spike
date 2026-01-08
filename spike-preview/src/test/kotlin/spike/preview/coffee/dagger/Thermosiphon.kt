package spike.preview.coffee.dagger

import dagger.Binds
import javax.inject.Inject

class Thermosiphon @Inject constructor(
    private val logger: CoffeeLogger,
    private val heater: Heater
) : Pump {
    override fun pump() {
        if (heater.isHot) {
            logger.log("=> => pumping => =>")
        }
    }

    @dagger.Module
    interface Module {
        @Binds
        fun heater(thermo: Thermosiphon): Pump
    }
}