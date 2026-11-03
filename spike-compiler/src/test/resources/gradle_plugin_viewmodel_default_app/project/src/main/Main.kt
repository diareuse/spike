import androidx.lifecycle.SavedStateHandle
import spike.lifecycle.viewmodel.ViewModelEntryPointImpl
import spike.lifecycle.viewmodel.factory

fun main() {
    val viewModels = ViewModelEntryPointImpl.factory().create(SavedStateHandle()).viewModels
    check(viewModels.keys == setOf(DefaultViewModel::class)) { "viewModels ${viewModels.keys}" }
}

@spike.lifecycle.viewmodel.SpikeViewModel
class DefaultViewModel : androidx.lifecycle.ViewModel()
