package com.example

import com.example.platform.OkHttpClient

@spike.Include
fun client(): OkHttpClient = OkHttpClient()

@spike.Include
class Lib {
    val name = "lib"
}

@spike.Export
interface Exported {
    fun lib(): Lib
    fun client(): OkHttpClient
}
