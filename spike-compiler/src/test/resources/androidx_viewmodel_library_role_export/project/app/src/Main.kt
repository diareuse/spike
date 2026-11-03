package com.example.app

import androidx.lifecycle.SavedStateHandle
import com.example.lib.LibraryViewModel
import spike.lifecycle.viewmodel.ViewModelEntryPointImpl
import spike.lifecycle.viewmodel.factory

fun main() {
    val viewModels = ViewModelEntryPointImpl.factory().create(SavedStateHandle()).viewModels
    check(viewModels.keys == setOf(LibraryViewModel::class, AppViewModel::class)) { "viewModels ${viewModels.keys}" }
    check(viewModels.getValue(LibraryViewModel::class).get() is LibraryViewModel)
}

@spike.lifecycle.viewmodel.SpikeViewModel
class AppViewModel : androidx.lifecycle.ViewModel()
