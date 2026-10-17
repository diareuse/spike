package com.example.test

import com.example.App

open class Clock

@spike.Include
@spike.test.Replaces(Clock::class)
class FakeClock : Clock()

@spike.EntryPoint
interface TestApp : App {
    companion object
}
