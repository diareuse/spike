package com.example.test

import com.example.App

@spike.EntryPoint
interface TestApp : App {
    companion object
}

@spike.EntryPoint
interface OtherTestApp : App {
    companion object
}
