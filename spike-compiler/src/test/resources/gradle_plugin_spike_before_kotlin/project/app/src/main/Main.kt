package com.example.app

import androidx.lifecycle.SavedStateHandle
import com.example.view.Greeting
import com.example.view.GreetingViewModel
import spike.lifecycle.viewmodel.ViewModelEntryPointImpl
import spike.lifecycle.viewmodel.factory

fun main() {
    val viewModels = ViewModelEntryPointImpl.factory().create(SavedStateHandle()).viewModels
    check(viewModels.keys == setOf(GreetingViewModel::class)) { "viewModels ${viewModels.keys}" }
    val viewModel = viewModels.getValue(GreetingViewModel::class).get() as GreetingViewModel
    check(viewModel.greeting.text == "hello") { "greeting ${viewModel.greeting.text}" }
}

@spike.Include
class AppGreeting : Greeting {
    override val text = "hello"
}

@spike.Include
val AppGreeting.asGreeting: Greeting get() = this
