package com.example.test

import com.example.App
import com.example.Screen

fun main() {
    check(TestApp().screen.name() == "fake") { "screen was ${TestApp().screen.name()}" }
}

@spike.Include
@spike.test.Replaces(Screen::class)
class FakeScreen : Screen {
    override fun name() = "fake"
}

@spike.EntryPoint
interface TestApp : App {
    companion object
}
