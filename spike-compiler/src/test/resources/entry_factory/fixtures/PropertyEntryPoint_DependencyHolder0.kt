import kotlin.Any
import kotlin.Array
import kotlin.Int

public object PropertyEntryPoint_DependencyHolder0 {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> Cat(buffer[0] as Dog)
    1 -> inputParameterDog()
    else -> error("Invalid position")
  }
}
