package com.example.feature

import com.example.library.Greeter

class Suffix(val value: String)

@spike.Export
interface WelcomeExport {
    val welcome: Welcome
}

@spike.Include
class Welcome(private val greeter: Greeter, private val suffix: Suffix) {
    fun show() = "${greeter.greet()}, ${suffix.value}"
}
