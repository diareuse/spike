import spike.lifecycle.viewmodel.invoke

fun main() {
    val vm = spike.lifecycle.viewmodel.ViewModelEntryPoint(androidx.lifecycle.SavedStateHandle()).viewModels[MyViewModel::class]!!.get() as MyViewModel
    check(vm.sayYes() == "yes")
}

@spike.lifecycle.viewmodel.SpikeViewModel
class MyViewModel(handle: androidx.lifecycle.SavedStateHandle) : androidx.lifecycle.ViewModel() {
    fun sayYes() = "yes"
}