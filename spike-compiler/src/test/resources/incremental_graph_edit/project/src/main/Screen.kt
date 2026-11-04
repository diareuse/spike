@spike.Include
class Screen(val clock: Clock) {
    val text get() = "${clock.name}"
}
