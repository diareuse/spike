package com.example.test

import com.example.App
import com.example.data.Source
import com.example.invoke

fun main() {
    val screen = TestApp().screen
    check(screen.source.name() == "function") { "source was ${screen.source.name()}" }
    check(screen.clock.name() == "library clock") { "clock was ${screen.clock.name()}" }
    check(App().screen.source.name() == "remote") { "production source was ${App().screen.source.name()}" }
}

@spike.Include
@spike.test.Replaces(Source::class)
fun fakeSource(): Source = object : Source {
    override fun name() = "function"
}

@spike.EntryPoint
interface TestApp : App {
    companion object
}
