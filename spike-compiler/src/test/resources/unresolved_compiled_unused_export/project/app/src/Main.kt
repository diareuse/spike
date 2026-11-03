package com.example.app

import com.example.Lib

fun main() {
    check(App().lib.name == "lib")
}

@spike.EntryPoint
interface App {
    val lib: Lib

    companion object
}
