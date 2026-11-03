package com.example

import com.example.platform.Keep
import com.example.platform.Reason

@spike.Include
fun name(): String = "screen"

@Keep(Reason::class)
@spike.Include
class Screen(@Keep val name: String)

@spike.EntryPoint
interface App {
    val screen: Screen

    companion object
}
