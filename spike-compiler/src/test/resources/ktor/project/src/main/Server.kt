import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

@spike.EntryPoint
interface RouteEntryPoint : spike.ktor.BaseRouteEntryPoint {
    companion object
}

@spike.Include(
    bindAs = spike.ktor.RouteBuilder::class,
    bindTo = spike.BindTarget.Set
)
class GetRoot : spike.ktor.RegularRouteBuilder {
    override val path: String = "/"
    override fun Route.build() {
        get {
            call.respond(HttpStatusCode.NoContent)
        }
    }
}