import androidx.lifecycle.SavedStateHandle
import spike.lifecycle.viewmodel.ViewModelEntryPointImpl
import spike.lifecycle.viewmodel.factory

fun main() {
    val fake = TestViewModels.factory().create(SavedStateHandle()).viewModels[AppViewModel::class]?.get()
    check((fake as? AppViewModel)?.greeting?.text() == "fake") { "test greeting ${(fake as? AppViewModel)?.greeting}" }
    val real = ViewModelEntryPointImpl.factory().create(SavedStateHandle()).viewModels[AppViewModel::class]?.get()
    check((real as? AppViewModel)?.greeting?.text() == "real") { "production greeting ${(real as? AppViewModel)?.greeting}" }
}

@spike.Include
@spike.test.Replaces(Greeting::class)
class FakeGreeting : Greeting {
    override fun text() = "fake"
}

@spike.EntryPoint
interface TestViewModels : ViewModelEntryPointImpl {
    @spike.EntryPoint.Factory
    interface Factory {
        fun create(handle: SavedStateHandle): TestViewModels
    }

    companion object
}
