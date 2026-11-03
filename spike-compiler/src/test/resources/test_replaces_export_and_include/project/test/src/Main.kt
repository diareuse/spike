package com.example.test

import com.example.App
import com.example.data.Source
import com.example.invoke

fun main() {
    TestApp().screen
}

@spike.Include
@spike.test.Replaces(Source::class)
class FakeSource : Source {
    override fun name() = "fake"
}

@spike.Include
fun otherSource(): Source = FakeSource()

@spike.EntryPoint
interface TestApp : App {
    companion object
}
