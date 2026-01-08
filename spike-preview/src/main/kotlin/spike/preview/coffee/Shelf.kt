package spike.preview.coffee

import spike.EntryPoint

@EntryPoint
interface Shelf {
    val coffeeMaker: CoffeeMaker
    fun logger(): CoffeeLogger

    companion object
}