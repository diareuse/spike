import kotlin.Lazy
import kotlin.collections.List
import kotlin.lazy
import spike.Provider

internal class SpikeDependencyContainer(
  private val listOfBattery: List<Battery>,
) {
  public val television: Television
    get() = samsungTelevision as Television

  public val remote: Remote
    get() = remoteControl as Remote

  private val samsungTelevision: SamsungTelevision by lazy {
        val lazyOfSoundSystem: Lazy<SoundSystem> = lazy(::soundSystem)
        SamsungTelevision(
          soundSystem = lazyOfSoundSystem
        )
      }

  private inline val remoteControl: RemoteControl
    get() {
      val providerOfTelevision: Provider<Television> = Provider(::television)
      val listOfBattery: List<Battery> = listOfBattery
      return RemoteControl(
        televisionProvider = providerOfTelevision,
        batteries = listOfBattery
      )
    }

  private inline val soundSystem: SoundSystem
    get() = amazonFireSoundBar as SoundSystem

  private inline val amazonFireSoundBar: AmazonFireSoundBar
    get() = AmazonFireSoundBar()
}
