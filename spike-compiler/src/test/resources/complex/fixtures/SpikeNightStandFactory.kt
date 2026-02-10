import kotlin.collections.List

internal object SpikeNightStandFactory : NightStand.Factory {
  override fun create(batteries: List<Battery>): NightStand = SpikeNightStand(SpikeDependencyContainer(batteries))
}
