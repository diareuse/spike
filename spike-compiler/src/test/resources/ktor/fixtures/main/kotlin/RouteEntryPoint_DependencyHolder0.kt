import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.collections.setOf

public class RouteEntryPoint_DependencyHolder0(
  private val factory: RouteEntryPoint_Factory,
) {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> setOf(buffer[0] as GetRoot)
    1 -> GetRoot()
    else -> error("Invalid position")
  }
}
