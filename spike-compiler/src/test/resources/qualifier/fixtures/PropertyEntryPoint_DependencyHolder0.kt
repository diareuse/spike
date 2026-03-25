import kotlin.Any
import kotlin.Array
import kotlin.Int

public object PropertyEntryPoint_DependencyHolder0 {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> buffer[0] as Cat
    1 -> Sphynx()
    2 -> buffer[0] as Cat
    3 -> defaultCat()
    else -> error("Invalid position")
  }
}
