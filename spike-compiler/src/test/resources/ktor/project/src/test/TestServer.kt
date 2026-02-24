import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.install
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class TestServer {

    @Test
    fun `check responds 204`() = testApplication {
        application {
            install(spike.ktor.Spike) {
                entryPoint(RouteEntryPoint())
            }
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.NoContent, response.status)
    }

}