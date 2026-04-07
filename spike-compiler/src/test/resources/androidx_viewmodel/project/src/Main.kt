import spike.lifecycle.viewmodel.invoke

fun main() {
    val vm = spike.lifecycle.viewmodel.ViewModelEntryPoint().viewModels[MyViewModel::class]!!.get() as MyViewModel
    check(vm.sayYes() == "yes")
}

@spike.lifecycle.viewmodel.SpikeViewModel
class MyViewModel : androidx.lifecycle.ViewModel() {
    fun sayYes() = "yes"
}