package com.example

import androidx.lifecycle.SavedStateHandle
import com.example.viewmodels.ViewModelEntryPointImpl
import com.example.viewmodels.factory

fun main() {
    val viewModels = ViewModelEntryPointImpl.factory().create(SavedStateHandle()).viewModels
    check(viewModels.keys == setOf(PackagedViewModel::class)) { "viewModels ${viewModels.keys}" }
}

@spike.lifecycle.viewmodel.SpikeViewModel
class PackagedViewModel : androidx.lifecycle.ViewModel()
