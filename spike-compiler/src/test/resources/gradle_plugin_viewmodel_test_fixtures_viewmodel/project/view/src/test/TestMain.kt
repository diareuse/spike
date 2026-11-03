package com.example.view

import androidx.lifecycle.SavedStateHandle
import spike.lifecycle.viewmodel.ViewModelEntryPointImpl
import spike.lifecycle.viewmodel.factory

fun main() {
    val viewModels = ViewModelEntryPointImpl.factory().create(SavedStateHandle()).viewModels
    check(viewModels.keys == setOf(GreetingViewModel::class, FixtureViewModel::class)) { "viewModels ${viewModels.keys}" }
}

@spike.Include
class TestGreeting : Greeting {
    override val text = "test"
}

@spike.Include
val TestGreeting.asGreeting: Greeting get() = this
