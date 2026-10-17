package spike.compiler.metro

import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionProblem

class ContributionCheckSingle : ConversionCheck<Contribution> {
    override fun check(subject: Contribution): ConversionProblem? {
        if (subject.supertypes.size == 1 && !subject.hasExplicitBinding) return null
        return ConversionProblem(
            "SPIKE-METRO-CONTRIBUTION",
            "Only contributions to a single supertype are converted; add an @Include binding for the others.",
            subject.declaration,
        )
    }
}
