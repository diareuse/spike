package com.example.test

import com.example.App
import com.example.data.Primary
import com.example.data.Source
import com.example.invoke

fun main() {
    val screen = TestApp().screen
    check(screen.primary.name() == "fake") { "primary was ${screen.primary.name()}" }
    check(screen.secondary.name() == "secondary") { "secondary was ${screen.secondary.name()}" }
    check(App().screen.primary.name() == "primary") { "production primary was ${App().screen.primary.name()}" }
}

@spike.Include
@Primary
@spike.test.Replaces(Source::class)
class FakeSource : Source {
    override fun name() = "fake"
}

@spike.EntryPoint
interface TestApp : App {
    companion object
}
