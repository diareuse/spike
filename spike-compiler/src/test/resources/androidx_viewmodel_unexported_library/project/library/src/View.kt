package com.example.view

@spike.Include
class Greeting

@spike.lifecycle.viewmodel.SpikeViewModel
class ViewViewModel(val greeting: Greeting) : androidx.lifecycle.ViewModel()
