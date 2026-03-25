fun main() {
    spike.lifecycle.viewmodel.ViewModelEntryPoint().viewModels[MyViewModel::class].get()
}

@spike.lifecycle.viewmodel.SpikeViewModel
class MyViewModel : androidx.lifecycle.ViewModel()