internal class SpikePropertyEntryPointEntryPoint(
  private val container: DependencyContainer,
) : PropertyEntryPoint {
  override val cat: Cat
    get() = container.cat
}

public operator fun PropertyEntryPoint.Companion.invoke(dog: Dog): PropertyEntryPoint = SpikePropertyEntryPointFactory().create(dog)
