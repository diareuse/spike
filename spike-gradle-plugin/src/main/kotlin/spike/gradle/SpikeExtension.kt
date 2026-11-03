package spike.gradle

import org.gradle.api.provider.Property

public abstract class SpikeExtension {
    public abstract val library: Property<Boolean>
}
