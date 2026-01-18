internal class SpikePropertyEntryPoint(
  private val container: SpikeDependencyContainer,
) : PropertyEntryPoint {
  override val cat: Cat
    get() = container.cat
}

public operator fun PropertyEntryPoint.Companion.invoke(dog: Dog): PropertyEntryPoint = SpikePropertyEntryPointFactory().create(dog)
