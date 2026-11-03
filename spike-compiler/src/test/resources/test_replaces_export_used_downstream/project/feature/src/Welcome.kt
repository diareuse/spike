package com.example.feature

import com.example.data.Source

@spike.Include
class Welcome(val source: Source)

@spike.Export
interface WelcomeExports {
    val welcome: Welcome
}
