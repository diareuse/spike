package spike.ktor

interface BaseRouteEntryPoint {
    val routes: Set<RouteBuilder>
}
