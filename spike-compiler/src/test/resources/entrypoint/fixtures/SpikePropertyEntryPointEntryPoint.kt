internal class SpikePropertyEntryPointEntryPoint(
  private val container: DependencyContainer,
) : PropertyEntryPoint {
  override val injectable: Injectable
    get() = container.injectable
}

public operator fun PropertyEntryPoint.Companion.invoke(): PropertyEntryPoint = SpikePropertyEntryPointEntryPoint(DependencyContainer())
