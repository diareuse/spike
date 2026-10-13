import androidx.lifecycle.SavedStateHandle
import spike.lifecycle.viewmodel.ViewModelEntryPointImpl
import spike.lifecycle.viewmodel.factory

fun main() {
    val factory = ViewModelEntryPointImpl.factory()
    val first = factory.create(SavedStateHandle()).viewModels[RepositoryViewModel::class]?.get() as RepositoryViewModel
    val second = factory.create(SavedStateHandle()).viewModels[RepositoryViewModel::class]?.get() as RepositoryViewModel
    require(first.repository === second.repository) { "each ViewModel got its own @Singleton Repository" }
    require(first.handle !== second.handle) { "ViewModels shared a SavedStateHandle" }
}

@spike.Singleton
@spike.Include
class Repository

@spike.lifecycle.viewmodel.SpikeViewModel
class RepositoryViewModel(
    val repository: Repository,
    val handle: SavedStateHandle,
) : androidx.lifecycle.ViewModel()
