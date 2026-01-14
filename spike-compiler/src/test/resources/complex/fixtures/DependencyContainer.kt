import kotlin.collections.List
import kotlin.lazy

internal class DependencyContainer(
  batteries: List<Battery>,
) {
  private val list_Battery_: List<Battery> = batteries

  public inline val television: Television
    get() = samsungTelevision as Television

  public inline val remote: Remote
    get() = remoteControl as Remote

  private val samsungTelevision: SamsungTelevision by lazy {
        SamsungTelevision(soundSystem = lazy(::soundSystem))
      }

  private inline val remoteControl: RemoteControl
    get() = RemoteControl(televisionProvider = ::television, batteries = list_Battery_)

  private inline val soundSystem: SoundSystem
    get() = amazonFireSoundBar as SoundSystem

  private inline val amazonFireSoundBar: AmazonFireSoundBar
    get() = AmazonFireSoundBar()
}
