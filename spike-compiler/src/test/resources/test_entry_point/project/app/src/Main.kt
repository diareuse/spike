package com.example.test

import com.example.App

fun main() {
    check(TestApp().screen.repo.name() == "real") { "repo was ${TestApp().screen.repo.name()}" }
}

@spike.EntryPoint
interface TestApp : App {
    companion object
}
