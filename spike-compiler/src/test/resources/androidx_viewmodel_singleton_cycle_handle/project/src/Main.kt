@spike.Include
class Screen(val handle: androidx.lifecycle.SavedStateHandle, val session: Session)

@spike.Singleton
@spike.Include
class Session(val screen: spike.Provider<Screen>)

@spike.lifecycle.viewmodel.SpikeViewModel
class ScreenViewModel(val screen: Screen) : androidx.lifecycle.ViewModel()
