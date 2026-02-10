internal class SpikePropertyEntryPoint(
  private val container: SpikeDependencyContainer,
) : PropertyEntryPoint {
  override val injectable: Injectable
    get() = container.injectable
}

public operator fun PropertyEntryPoint.Companion.invoke(): PropertyEntryPoint = SpikePropertyEntryPointFactory.create()
