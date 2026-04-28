@file:Suppress("UNCHECKED_CAST", "unused", "RedundantVisibilityModifier", "ClassName")

import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.Lazy
import kotlin.Suppress
import kotlin.collections.List
import kotlin.lazy
import spike.Provider
import spike.factory.DependencyId
import spike.factory.SingletonHolder

public class NightStand_DependencyHolder0(
  private val factory: NightStand_Factory,
) {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> buffer[0] as Television
    1 -> lazy { factory.get<SoundSystem>(DependencyId(3)) }
    2 -> singletons.getOrPut(2) {
        SamsungTelevision(buffer[0] as Lazy<SoundSystem>)
    }
    3 -> buffer[0] as SoundSystem
    4 -> AmazonFireSoundBar()
    5 -> buffer[0] as Remote
    6 -> batteries()
    7 -> Provider { factory.get<Television>(DependencyId(0)) }
    8 -> RemoteControl(buffer[0] as Provider<Television>, buffer[1] as List<Battery>)
    else -> error("Invalid position")
  }

  public companion object {
    private val singletons: SingletonHolder = SingletonHolder()
  }
}
