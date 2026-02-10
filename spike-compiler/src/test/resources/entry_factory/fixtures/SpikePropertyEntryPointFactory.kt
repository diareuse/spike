internal object SpikePropertyEntryPointFactory : PropertyEntryPoint.Factory {
  override fun create(dog: Dog): PropertyEntryPoint = SpikePropertyEntryPoint(SpikeDependencyContainer(dog))
}
