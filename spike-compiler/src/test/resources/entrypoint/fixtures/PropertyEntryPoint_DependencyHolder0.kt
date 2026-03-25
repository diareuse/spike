import kotlin.Any
import kotlin.Array
import kotlin.Int

public object PropertyEntryPoint_DependencyHolder0 {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> Injectable()
    else -> error("Invalid position")
  }
}
