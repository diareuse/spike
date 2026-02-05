internal class SpikePropertyEntryPointFactory {
  public fun create(): PropertyEntryPoint = SpikePropertyEntryPoint(SpikeDependencyContainer())
}
