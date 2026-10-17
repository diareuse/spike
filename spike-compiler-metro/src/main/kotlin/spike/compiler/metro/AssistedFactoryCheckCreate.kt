package spike.compiler.metro

import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionProblem

class AssistedFactoryCheckCreate(private val code: String, private val message: String) :
    ConversionCheck<MetroAssistedFactory> {
    override fun check(subject: MetroAssistedFactory): ConversionProblem? {
        if (subject.create != null) return null
        return ConversionProblem(code, message, subject.declaration)
    }
}
