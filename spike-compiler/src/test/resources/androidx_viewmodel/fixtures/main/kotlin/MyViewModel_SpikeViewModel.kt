import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import spike.BindTarget
import spike.Include
import spike.lifecycle.viewmodel.ViewModelKey

@Include(bindTo = BindTarget.Map)
@ViewModelKey(MyViewModel::class)
public fun spikeViewModelMyViewModel(handle: SavedStateHandle): ViewModel = MyViewModel(handle)
