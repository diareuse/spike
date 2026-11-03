package com.example.app

import androidx.lifecycle.SavedStateHandle
import com.example.view.FixtureGreeting
import com.example.view.GreetingViewModel
import spike.lifecycle.viewmodel.ViewModelEntryPointImpl
import spike.lifecycle.viewmodel.factory

fun main() {
    check(FixtureGreeting().text == "fixture")
    val viewModels = ViewModelEntryPointImpl.factory().create(SavedStateHandle()).viewModels
    check(viewModels.keys == setOf(GreetingViewModel::class)) { "viewModels ${viewModels.keys}" }
}
