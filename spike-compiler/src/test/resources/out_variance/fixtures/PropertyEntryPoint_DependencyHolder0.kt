import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.collections.mapOf
import kotlin.reflect.KClass

public object PropertyEntryPoint_DependencyHolder0 {
  internal fun create(buffer: Array<Any?>, position: Int): Any = when(position) {
    0 -> mapOf<KClass<out Cat>, Cat>(
      SomeCat::class to buffer[0] as SomeCat
    )
    1 -> SomeCat()
    else -> error("Invalid position")
  }
}
