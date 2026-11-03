package com.example.test

import com.example.App

class Bar

@spike.Include
fun bar(): Bar = Bar()

@spike.Include
fun otherBar(): Bar = Bar()

@spike.EntryPoint
interface TestApp : App {
    companion object
}

@spike.EntryPoint
interface Bars {
    val bar: Bar

    companion object
}
