package spike.ktor

import io.ktor.server.application.ApplicationPlugin
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.routing.routing

private val SpikePlugin = createApplicationPlugin(
    name = "spike",
    createConfiguration = { SpikeConfig() }
) {
    application.routing {
        val ep = checkNotNull(pluginConfig.entryPoint) {
            "Create an entry point with 'entryPoint(RouteEntryPoint())'"
        }
        spike(ep)
    }
}

object Spike : ApplicationPlugin<SpikeConfig> by SpikePlugin

class SpikeConfig internal constructor() {

    internal var entryPoint: BaseRouteEntryPoint? = null
    fun entryPoint(entryPoint: BaseRouteEntryPoint) {
        this.entryPoint = entryPoint
    }

}
