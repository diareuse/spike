package spike.gradle

import spike.Include
import java.util.Properties

@Include
internal class SpikeVersionResource : SpikeVersion {
    override fun value(): String {
        val properties = Properties()
        SpikePlugin::class.java.getResourceAsStream("/spike-gradle-plugin.properties")?.use(properties::load)
        return checkNotNull(properties.getProperty("version")) { "spike-gradle-plugin.properties is missing" }
    }
}
