package com.example.test

import com.example.App

fun main() {
    check(TestApp().screen.name == "screen")
}

@spike.EntryPoint
interface TestApp : App {
    companion object
}
