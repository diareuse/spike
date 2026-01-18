import kotlin.collections.List

internal class SpikeNightStand(
  private val container: SpikeDependencyContainer,
) : NightStand {
  override val television: Television
    get() = container.television

  override val remote: Remote
    get() = container.remote
}

public operator fun NightStand.Companion.invoke(batteries: List<Battery>): NightStand = SpikeNightStandFactory().create(batteries)
