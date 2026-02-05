internal class SpikePropertyEntryPoint(
  private val container: SpikeDependencyContainer,
) : PropertyEntryPoint {
  override val obj: ComplexObject
    get() = container.complexObject
}

public operator fun PropertyEntryPoint.Companion.invoke(): PropertyEntryPoint = SpikePropertyEntryPointFactory().create()
