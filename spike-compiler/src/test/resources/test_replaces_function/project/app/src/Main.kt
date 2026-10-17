package com.example.test

import com.example.App
import com.example.Repo

fun main() {
    check(TestApp().screen.repo.name() == "function") { "repo was ${TestApp().screen.repo.name()}" }
}

@spike.Include
@spike.test.Replaces(Repo::class)
fun fakeRepo(): Repo = object : Repo {
    override fun name() = "function"
}

@spike.EntryPoint
interface TestApp : App {
    companion object
}
