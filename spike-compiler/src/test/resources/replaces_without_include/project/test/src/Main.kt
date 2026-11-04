package com.example.test

import com.example.invoke

fun main() {
    check(TestApp().screen.repo.name() == "real") { TestApp().screen.repo.name() }
}
