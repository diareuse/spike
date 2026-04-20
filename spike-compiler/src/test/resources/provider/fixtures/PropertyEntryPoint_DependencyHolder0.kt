import kotlin.Any
import kotlin.Array
import kotlin.Int
import spike.Provider
import spike.factory.DependencyId

public class PropertyEntryPoint_DependencyHolder0(
  private val factory: PropertyEntryPoint_Factory,
) {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> Cat(buffer[0] as Provider<Food>)
    1 -> Provider { factory.get<Food>(DependencyId(2)) }
    2 -> Food()
    else -> error("Invalid position")
  }
}
