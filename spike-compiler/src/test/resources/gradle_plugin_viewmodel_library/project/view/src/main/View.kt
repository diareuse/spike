package com.example.view

interface Greeting {
    val text: String
}

@spike.lifecycle.viewmodel.SpikeViewModel
class GreetingViewModel(val greeting: Greeting) : androidx.lifecycle.ViewModel()
