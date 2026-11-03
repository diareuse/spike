package com.example.lib

@spike.Export
interface Exported {
    val lib: Lib
}

@spike.Include
class Lib

@spike.lifecycle.viewmodel.SpikeViewModel
class LibraryViewModel(val lib: Lib) : androidx.lifecycle.ViewModel()
