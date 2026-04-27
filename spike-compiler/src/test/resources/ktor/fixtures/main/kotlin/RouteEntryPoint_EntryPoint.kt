import kotlin.collections.Set
import spike.ktor.RouteBuilder

private class RouteEntryPoint_EntryPoint(
  private val factory: RouteEntryPoint_Factory,
) : RouteEntryPoint {
  override val routes: Set<RouteBuilder>
    get() = factory.get(spike.factory.DependencyId(0))

  public object Factory {
    public fun create(): RouteEntryPoint_EntryPoint = RouteEntryPoint_EntryPoint(RouteEntryPoint_Factory())
  }
}

public operator fun RouteEntryPoint.Companion.invoke(): RouteEntryPoint = RouteEntryPoint_EntryPoint.Factory.create()
