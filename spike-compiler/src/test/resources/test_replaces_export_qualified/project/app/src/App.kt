package com.example

import com.example.data.Primary
import com.example.data.Secondary
import com.example.data.Source

@spike.Include
class Screen(@Primary val primary: Source, @Secondary val secondary: Source)

@spike.EntryPoint
interface App {
    val screen: Screen

    companion object
}
