fun main() {
    val app = TestApp()
    check(app.dollar.name == "dollar") { app.dollar.name }
    check(app.middle.name == "middle") { app.middle.name }
    check(app.wrap.name == "wrap") { app.wrap.name }
}

@spike.Include
@spike.test.Replaces(`Dollar$Name`::class)
class FakeDollar : `Dollar$Name`() {
    val name = "dollar"
}

@spike.Include
@spike.test.Replaces(`Middle·Dot`::class)
class FakeMiddle : `Middle·Dot`() {
    val name = "middle"
}

@spike.Include
@spike.test.Replaces(`Wrap♢Point`::class)
class FakeWrap : `Wrap♢Point`() {
    val name = "wrap"
}

@spike.EntryPoint
interface TestApp : App {
    val dollar: FakeDollar
    val middle: FakeMiddle
    val wrap: FakeWrap

    companion object
}
