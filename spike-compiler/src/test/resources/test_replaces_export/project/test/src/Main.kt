package com.example.test

import com.example.App
import com.example.data.Source
import com.example.invoke

fun main() {
    val screen = TestApp().screen
    check(screen.source.name() == "fake") { "source was ${screen.source.name()}" }
    check(screen.clock.name() == "library clock") { "clock was ${screen.clock.name()}" }
    check(App().screen.source.name() == "remote") { "production source was ${App().screen.source.name()}" }
}

@spike.Include
@spike.test.Replaces(Source::class)
class FakeSource : Source {
    override fun name() = "fake"
}

@spike.EntryPoint
interface TestApp : App {
    companion object
}
