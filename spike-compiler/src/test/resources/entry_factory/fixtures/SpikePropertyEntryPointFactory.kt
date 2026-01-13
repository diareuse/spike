internal class SpikePropertyEntryPointFactory : PropertyEntryPoint.Factory {
  override fun create(dog: Dog): PropertyEntryPoint = SpikePropertyEntryPointEntryPoint(DependencyContainer(dog))
}
