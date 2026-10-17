package com.example.test

import com.example.App
import com.example.Repo
import com.example.invoke

fun main() {
    check(TestApp().screen.repo.name() == "fake") { "repo was ${TestApp().screen.repo.name()}" }
    check(App().screen.repo.name() == "real") { "production repo was ${App().screen.repo.name()}" }
}

@spike.Include
@spike.test.Replaces(Repo::class)
class FakeRepo : Repo {
    override fun name() = "fake"
}

@spike.EntryPoint
interface TestApp : App {
    companion object
}
