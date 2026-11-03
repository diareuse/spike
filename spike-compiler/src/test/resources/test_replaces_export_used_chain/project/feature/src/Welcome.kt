package com.example.feature

import com.example.data.Repository

@spike.Include
class Welcome(val repository: Repository)

@spike.Export
interface WelcomeExports {
    val welcome: Welcome
}
