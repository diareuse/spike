import kotlin.collections.List

internal class SpikeNightStandFactory : NightStand.Factory {
  override fun create(batteries: List<Battery>): NightStand = SpikeNightStand(SpikeDependencyContainer(batteries))
}
