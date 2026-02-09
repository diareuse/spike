@spike.EntryPoint
abstract class PropertyEntryPoint {
    abstract val injectable: Injectable

    companion object
}

@spike.Include
class Injectable