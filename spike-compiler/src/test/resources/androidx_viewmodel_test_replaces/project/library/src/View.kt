package com.example.view

interface Greeting {
    fun text(): String
}

@spike.Include
class RealGreeting : Greeting {
    override fun text() = "real"
}

@spike.Include
val RealGreeting.asGreeting: Greeting get() = this

@spike.lifecycle.viewmodel.SpikeViewModel
class GreetingViewModel(val greeting: Greeting) : androidx.lifecycle.ViewModel()
