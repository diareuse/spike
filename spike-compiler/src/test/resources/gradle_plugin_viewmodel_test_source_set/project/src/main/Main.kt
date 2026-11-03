import androidx.lifecycle.SavedStateHandle
import spike.lifecycle.viewmodel.ViewModelEntryPointImpl
import spike.lifecycle.viewmodel.factory

fun main() {
    val viewModels = ViewModelEntryPointImpl.factory().create(SavedStateHandle()).viewModels
    check(viewModels.keys == setOf(AppViewModel::class)) { "viewModels ${viewModels.keys}" }
}

interface Greeting {
    fun text(): String
}

@spike.Include
class RealGreeting : Greeting {
    override fun text() = "real"
}

@spike.Include
val RealGreeting.asGreeting: Greeting get() = this

@spike.lifecycle.viewmodel.SpikeViewModel
class AppViewModel(val greeting: Greeting) : androidx.lifecycle.ViewModel()
