import kotlin.Any
import kotlin.Array
import kotlin.Int

public class CatEP_DependencyHolder0(
  private val factory: CatEP_Factory,
) {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> Cat(buffer[0] as Life, buffer[1] as Life, buffer[2] as Life)
    1 -> Life()
    else -> error("Invalid position")
  }
}
