import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.Lazy
import kotlin.collections.List
import kotlin.lazy
import spike.Provider
import spike.factory.DependencyId

public object NightStand_DependencyHolder0 {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> buffer[0] as Television
    1 -> lazy { NightStand_Factory.get<SoundSystem>(DependencyId(3)) }
    2 -> SamsungTelevision(buffer[0] as Lazy<SoundSystem>)
    3 -> buffer[0] as SoundSystem
    4 -> AmazonFireSoundBar()
    5 -> buffer[0] as Remote
    6 -> batteries()
    7 -> Provider { NightStand_Factory.get<Television>(DependencyId(9)) }
    8 -> RemoteControl(buffer[0] as Provider<Television>, buffer[1] as List<Battery>)
    9 -> buffer[0] as Television
    else -> error("Invalid position")
  }
}
