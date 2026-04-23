import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.lazy
import spike.Provider
import spike.factory.DependencyId
import spike.factory.SingletonHolder

public class PropertyEntryPoint_DependencyHolder0(
  private val factory: PropertyEntryPoint_Factory,
) {
  private val singletons: SingletonHolder = SingletonHolder()

  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> singletons.getOrPut(0) {
        Cat(buffer[0] as Provider<Food>)
    }
    1 -> Provider { factory.get<Food>(DependencyId(2)) }
    2 -> Food()
    3 -> Provider { factory.get<Cat>(DependencyId(0)) }
    4 -> lazy { factory.get<Cat>(DependencyId(0)) }
    else -> error("Invalid position")
  }
}
