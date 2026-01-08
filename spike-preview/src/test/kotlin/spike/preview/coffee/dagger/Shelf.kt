package spike.preview.coffee.dagger

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ElectricHeater.Module::class, Thermosiphon.Module::class])
interface Shelf {
    val coffeeMaker: CoffeeMaker
    fun logger(): CoffeeLogger
}