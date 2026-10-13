package com.example.app

import androidx.lifecycle.SavedStateHandle
import com.example.lib.Lib
import spike.lifecycle.viewmodel.ViewModelEntryPointImpl
import spike.lifecycle.viewmodel.factory

fun main() {
    val factory = ViewModelEntryPointImpl.factory()
    val first = factory.create(SavedStateHandle()).viewModels[LibViewModel::class]?.get() as LibViewModel
    val second = factory.create(SavedStateHandle()).viewModels[LibViewModel::class]?.get() as LibViewModel
    require(first.lib === second.lib) { "each ViewModel got its own library @Singleton" }
}

@spike.lifecycle.viewmodel.SpikeViewModel
class LibViewModel(val lib: Lib) : androidx.lifecycle.ViewModel()
