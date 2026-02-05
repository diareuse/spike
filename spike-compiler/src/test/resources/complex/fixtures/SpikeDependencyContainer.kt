import kotlin.collections.List
import kotlin.lazy
import spike.Provider

internal class SpikeDependencyContainer(
  private val listOfBattery: List<Battery>,
) {
  public val television: Television by lazy {
        val lazyOfSoundSystem = lazy {
            AmazonFireSoundBar()
        }
        SamsungTelevision(
          soundSystem = lazyOfSoundSystem
        )
      }

  public inline val remote: Remote
    get() {
      val providerOfTelevision = Provider<Television> {
          val lazyOfSoundSystem = lazy {
              AmazonFireSoundBar()
          }
          SamsungTelevision(
            soundSystem = lazyOfSoundSystem
          )
      }
      return RemoteControl(
        televisionProvider = providerOfTelevision,
        batteries = listOfBattery
      )
    }
}
