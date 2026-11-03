package com.example.test

import androidx.lifecycle.SavedStateHandle
import com.example.app.AppViewModel
import com.example.lib.LibraryViewModel
import spike.lifecycle.viewmodel.ViewModelEntryPointImpl
import spike.lifecycle.viewmodel.factory

fun main() {
    val viewModels = ViewModelEntryPointImpl.factory().create(SavedStateHandle()).viewModels
    check(viewModels.keys == setOf(AppViewModel::class, LibraryViewModel::class)) { "viewModels ${viewModels.keys}" }
}
