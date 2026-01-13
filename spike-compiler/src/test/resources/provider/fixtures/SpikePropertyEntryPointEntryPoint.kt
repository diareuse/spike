internal class SpikePropertyEntryPointEntryPoint(
  private val container: DependencyContainer,
) : PropertyEntryPoint {
  override val cat: Cat
    get() = container.cat
}

public operator fun PropertyEntryPoint.Companion.invoke(): PropertyEntryPoint = SpikePropertyEntryPointEntryPoint(DependencyContainer())
