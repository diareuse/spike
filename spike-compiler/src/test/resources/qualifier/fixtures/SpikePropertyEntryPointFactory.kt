internal object SpikePropertyEntryPointFactory {
  public fun create(): PropertyEntryPoint = SpikePropertyEntryPoint(SpikeDependencyContainer())
}
