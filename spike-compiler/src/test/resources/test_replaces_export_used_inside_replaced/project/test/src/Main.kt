package com.example.test

import com.example.App
import com.example.data.Repository
import com.example.data.Source
import com.example.invoke

fun main() {
    val screen = TestApp().screen
    check(screen.source.name() == "fake") { "source was ${screen.source.name()}" }
    check(screen.repository.source.name() == "fake") { "repository source was ${screen.repository.source.name()}" }
    check(App().screen.repository.source.name() == "remote") { "production repository source changed" }
}

@spike.Include
@spike.test.Replaces(Source::class)
class FakeSource : Source {
    override fun name() = "fake"
}

@spike.Include
@spike.test.Replaces(Repository::class)
fun fakeRepository(source: Source) = Repository(source)

@spike.EntryPoint
interface TestApp : App {
    companion object
}
