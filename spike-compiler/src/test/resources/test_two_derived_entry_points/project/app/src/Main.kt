package com.example.test

import com.example.App
import com.example.Repo

fun main() {
    check(FakeApp().screen.repo.name() == "fake") { "fake app repo was ${FakeApp().screen.repo.name()}" }
    check(SecondApp().screen.repo.name() == "fake") { "second app repo was ${SecondApp().screen.repo.name()}" }
}

@spike.Include
@spike.test.Replaces(Repo::class)
class FakeRepo : Repo {
    override fun name() = "fake"
}

@spike.EntryPoint
interface FakeApp : App {
    companion object
}

@spike.EntryPoint
interface SecondApp : App {
    companion object
}
