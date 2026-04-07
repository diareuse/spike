import kotlin.collections.Map
import kotlin.reflect.KClass

private object PropertyEntryPoint_EntryPoint : PropertyEntryPoint {
  override val cats: Map<KClass<out Cat>, Cat>
    get() = PropertyEntryPoint_Factory.get(spike.factory.DependencyId(0))
}

public operator fun PropertyEntryPoint.Companion.invoke(): PropertyEntryPoint = PropertyEntryPoint_EntryPoint
