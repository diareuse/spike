internal class SpikePropertyEntryPoint(
  private val container: SpikeDependencyContainer,
) : PropertyEntryPoint {
  override val cat: Cat
    get() = container.cat
}

public val PropertyEntryPoint.Companion.factory: PropertyEntryPoint.Factory
  get() = SpikePropertyEntryPointFactory

public operator fun PropertyEntryPoint.Companion.invoke(dog: Dog): PropertyEntryPoint = factory.create(dog)
