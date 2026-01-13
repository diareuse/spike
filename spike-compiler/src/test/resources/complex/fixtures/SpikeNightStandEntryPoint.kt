import kotlin.collections.List

internal class SpikeNightStandEntryPoint(
  private val container: DependencyContainer,
) : NightStand {
  override val television: Television
    get() = container.television

  override val remote: Remote
    get() = container.remote
}

public operator fun NightStand.Companion.invoke(batteries: List<Battery>): NightStand = SpikeNightStandFactory().create(batteries)
