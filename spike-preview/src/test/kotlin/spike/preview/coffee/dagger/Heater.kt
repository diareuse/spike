package spike.preview.coffee.dagger

interface Heater {
    fun on()
    fun off()
    val isHot: Boolean
}