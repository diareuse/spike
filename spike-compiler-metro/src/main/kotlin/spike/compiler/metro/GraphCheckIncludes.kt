package spike.compiler.metro

import spike.Include
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionProblem

@Include
class GraphCheckIncludes : ConversionCheck<MetroGraph> {
    override fun check(subject: MetroGraph): ConversionProblem? {
        if (subject.inputs.none(MetroNames.Includes::annotates)) return null
        return ConversionProblem(
            "SPIKE-METRO-INCLUDES",
            "@Includes graph inputs are not converted; pass the needed values as @Provides parameters instead.",
            subject.declaration,
        )
    }
}
