package com.example.test

import androidx.lifecycle.SavedStateHandle
import com.example.view.Greeting
import com.example.view.GreetingViewModel
import spike.lifecycle.viewmodel.ViewModelEntryPointImpl
import spike.lifecycle.viewmodel.factory

fun main() {
    val viewModels = TestViewModels.factory().create(SavedStateHandle()).viewModels
    val viewModel = viewModels[GreetingViewModel::class]?.get() as? GreetingViewModel
    check(viewModel?.greeting?.text() == "fake") { "viewModels ${viewModels.keys}, greeting ${viewModel?.greeting}" }
    val production = ViewModelEntryPointImpl.factory().create(SavedStateHandle()).viewModels
    val real = production[GreetingViewModel::class]?.get() as? GreetingViewModel
    check(real?.greeting?.text() == "real") { "production viewModels ${production.keys}, greeting ${real?.greeting}" }
}

@spike.Include
@spike.test.Replaces(Greeting::class)
class FakeGreeting : Greeting {
    override fun text() = "fake"
}

@spike.EntryPoint
interface TestViewModels : ViewModelEntryPointImpl {
    @spike.EntryPoint.Factory
    interface Factory {
        fun create(handle: SavedStateHandle): TestViewModels
    }

    companion object
}
