package com.example

import com.example.data.Source
import com.example.feature.Welcome

@spike.Include
class Screen(val source: Source, val welcome: Welcome)

@spike.EntryPoint
interface App {
    val screen: Screen

    companion object
}
