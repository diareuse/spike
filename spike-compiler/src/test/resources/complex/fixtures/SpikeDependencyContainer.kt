import kotlin.collections.List
import kotlin.lazy

internal class SpikeDependencyContainer(
  private val listOfBattery: List<Battery>,
) {
  public val television: Television
    get() = samsungTelevision as Television

  public val remote: Remote
    get() = remoteControl as Remote

  private val samsungTelevision: SamsungTelevision by lazy {
        SamsungTelevision(soundSystem = lazy(::soundSystem))}

  private inline val remoteControl: RemoteControl
    get() = RemoteControl(televisionProvider = ::television, batteries = listOfBattery)

  private inline val soundSystem: SoundSystem
    get() = amazonFireSoundBar as SoundSystem

  private inline val amazonFireSoundBar: AmazonFireSoundBar
    get() = AmazonFireSoundBar()
}
