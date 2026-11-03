package com.example.view

import androidx.lifecycle.SavedStateHandle
import spike.lifecycle.viewmodel.ViewModelEntryPointImpl
import spike.lifecycle.viewmodel.factory

fun main() {
    val viewModels = ViewModelEntryPointImpl.factory().create(SavedStateHandle()).viewModels
    check(viewModels.keys == setOf(GreetingViewModel::class)) { "viewModels ${viewModels.keys}" }
    val viewModel = viewModels.getValue(GreetingViewModel::class).get() as GreetingViewModel
    check(viewModel.greeting.text == "test") { "greeting ${viewModel.greeting.text}" }
}

@spike.Include
class TestGreeting : Greeting {
    override val text = "test"
}

@spike.Include
val TestGreeting.asGreeting: Greeting get() = this
