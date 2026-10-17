package spike.compiler.dagger

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.SymbolProcessor
import spike.BindTarget
import spike.Include
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionCheckFirst
import spike.compiler.conversion.ConversionCheckMapped
import spike.compiler.conversion.EntryPointConversionFile
import spike.compiler.conversion.EntryPointTypeDecorator
import spike.compiler.conversion.EntryPointTypeInterface
import spike.compiler.conversion.EntryPointTypeOriginating
import spike.compiler.conversion.EntryPointTypeSupertype
import spike.compiler.conversion.Order
import spike.compiler.conversion.ProblemReporter
import spike.compiler.conversion.SymbolProcessorConversion
import spike.compiler.conversion.SymbolSourceAnnotated
import spike.compiler.conversion.applyTo
import spike.compiler.conversion.ordered

@Include(bindTo = BindTarget.Map)
@Order(20)
fun daggerComponents(
    checks: Map<Int, ConversionCheck<DaggerComponent>>,
    decorators: Map<Int, EntryPointTypeDecorator<DaggerComponent>>,
    reporter: ProblemReporter,
    codeGenerator: CodeGenerator,
): SymbolProcessor = SymbolProcessorConversion(
    SymbolSourceAnnotated(DaggerNames.Component),
    ConversionCheckMapped(DaggerComponent::of, ConversionCheckFirst(checks.ordered())),
    EntryPointConversionFile(DaggerComponent::of, decorators.applyTo(EntryPointTypeInterface()), codeGenerator),
    reporter,
)

@Include(bindTo = BindTarget.Map)
@Order(10)
fun daggerSupertype(): EntryPointTypeDecorator<DaggerComponent> = EntryPointTypeDecorator {
    EntryPointTypeSupertype(it)
}

@Include(bindTo = BindTarget.Map)
@Order(30)
fun daggerComponentOriginating(): EntryPointTypeDecorator<DaggerComponent> = EntryPointTypeDecorator {
    EntryPointTypeOriginating(it)
}
