@spike.Singleton
@spike.Include
class Session(val handle: androidx.lifecycle.SavedStateHandle)

@spike.lifecycle.viewmodel.SpikeViewModel
class SessionViewModel(val session: Lazy<Session>) : androidx.lifecycle.ViewModel()
