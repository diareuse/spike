import androidx.lifecycle.SavedStateHandle
import spike.lifecycle.viewmodel.ViewModelEntryPointImpl
import spike.lifecycle.viewmodel.invoke

fun main() {
    val first = ViewModelEntryPointImpl(SavedStateHandle()).viewModels[RepositoryViewModel::class]?.get() as RepositoryViewModel
    val second = ViewModelEntryPointImpl(SavedStateHandle()).viewModels[RepositoryViewModel::class]?.get() as RepositoryViewModel
    require(first.repository === second.repository) { "each ViewModel got its own @Singleton Repository" }
    require(first.repository === AppEntryPoint().repository) { "ViewModels and the app got different @Singleton Repository instances" }
}

@spike.EntryPoint
interface AppEntryPoint {
    val repository: Repository

    companion object
}

@spike.Singleton
@spike.Include
class Repository

@spike.lifecycle.viewmodel.SpikeViewModel
class RepositoryViewModel(val repository: Repository) : androidx.lifecycle.ViewModel()
