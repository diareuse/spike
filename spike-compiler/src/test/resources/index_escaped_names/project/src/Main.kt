fun main() {
    check(Odd().plain.dollar.text == "dollar") { "dollar ${Odd().plain.dollar.text}" }
}

@spike.Include
class `Dollar$Name` {
    val text = "dollar"
}

@spike.Include
class `Quote"Name`

@spike.Include
class `Tab	Name`

@spike.Include
class `BellName`

@spike.Include
class `Ünicode Name`

@spike.Include
class `Middle·Dot`

@spike.Include
class `Wrap♢Point`

@spike.Include
class Plain(
    val dollar: `Dollar$Name`,
    val quote: `Quote"Name`,
    val tab: `Tab	Name`,
    val unicode: `Ünicode Name`,
)

@spike.EntryPoint
interface Odd {
    val plain: Plain

    companion object
}
