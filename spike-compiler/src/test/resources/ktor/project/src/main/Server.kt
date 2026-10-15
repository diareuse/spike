import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

@spike.EntryPoint
interface RouteEntryPoint : spike.ktor.BaseRouteEntryPoint {
    companion object
}

@spike.Include(bindTo = spike.BindTarget.Set)
fun getRoot(): spike.ktor.RouteBuilder = GetRoot()

class GetRoot : spike.ktor.RegularRouteBuilder {
    override val path: String = "/"
    override fun Route.build() {
        get {
            call.respond(HttpStatusCode.NoContent)
        }
    }
}