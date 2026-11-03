package com.example.app

import com.example.Lib

@spike.EntryPoint
interface App {
    val lib: Lib

    companion object
}

@spike.EntryPoint
interface Settings {
    val lib: Lib

    companion object
}
