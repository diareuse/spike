package spike.compiler.conversion

import com.google.devtools.ksp.symbol.ClassKind

/**
 * Rejects a provision declared in the body of a class annotated with one of [parents], which only an instance of
 * that class could call.
 */
class ProvisionCheckParent(
    private val parents: AnnotationNames,
    private val code: String,
    private val message: String,
) : ConversionCheck<Provision> {
    override fun check(subject: Provision): ConversionProblem? {
        val parent = subject.parent?.takeIf { it.classKind != ClassKind.OBJECT }
        if (parent == null || !parents.annotates(parent)) return null
        return ConversionProblem(code, message, subject.declaration)
    }
}
