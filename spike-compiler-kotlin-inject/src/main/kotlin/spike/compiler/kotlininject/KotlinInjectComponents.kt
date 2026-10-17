package spike.compiler.kotlininject

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.SymbolProcessor
import spike.BindTarget
import spike.Include
import spike.compiler.conversion.AnnotationNames
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ConversionCheckFirst
import spike.compiler.conversion.ConversionCheckMapped
import spike.compiler.conversion.EntryPointConversionFile
import spike.compiler.conversion.EntryPointTypeDecorator
import spike.compiler.conversion.EntryPointTypeFunctions
import spike.compiler.conversion.EntryPointTypeInterface
import spike.compiler.conversion.EntryPointTypeOriginating
import spike.compiler.conversion.EntryPointTypeProperties
import spike.compiler.conversion.Order
import spike.compiler.conversion.ProblemReporter
import spike.compiler.conversion.SymbolProcessorConversion
import spike.compiler.conversion.SymbolSourceAnnotated
import spike.compiler.conversion.SymbolSourceFiltered
import spike.compiler.conversion.applyTo
import spike.compiler.conversion.ordered

@Include(bindTo = BindTarget.Map)
@Order(30)
fun kotlinInjectComponents(
    checks: Map<Int, ConversionCheck<KotlinInjectComponent>>,
    decorators: Map<Int, EntryPointTypeDecorator<KotlinInjectComponent>>,
    reporter: ProblemReporter,
    codeGenerator: CodeGenerator,
): SymbolProcessor {
    val source = SymbolSourceFiltered(SymbolSourceAnnotated(KotlinInjectNames.Component)) { symbol ->
        KotlinInjectComponent.of(symbol) != null
    }
    val check = ConversionCheckMapped(KotlinInjectComponent::of, ConversionCheckFirst(checks.ordered()))
    val type = decorators.applyTo(EntryPointTypeInterface())
    val conversion = EntryPointConversionFile(KotlinInjectComponent::of, type, codeGenerator)
    return SymbolProcessorConversion(source, check, conversion, reporter)
}

@Include(bindTo = BindTarget.Map)
@Order(10)
fun kotlinInjectProperties(): EntryPointTypeDecorator<KotlinInjectComponent> =
    EntryPointTypeDecorator { EntryPointTypeProperties(AnnotationNames(), it) }

@Include(bindTo = BindTarget.Map)
@Order(20)
fun kotlinInjectFunctions(): EntryPointTypeDecorator<KotlinInjectComponent> =
    EntryPointTypeDecorator { EntryPointTypeFunctions(AnnotationNames(), it) }

@Include(bindTo = BindTarget.Map)
@Order(30)
fun kotlinInjectInputs(): EntryPointTypeDecorator<KotlinInjectComponent> =
    EntryPointTypeDecorator { EntryPointTypeInputs(it) }

@Include(bindTo = BindTarget.Map)
@Order(40)
fun kotlinInjectComponentOriginating(): EntryPointTypeDecorator<KotlinInjectComponent> =
    EntryPointTypeDecorator { EntryPointTypeOriginating(it) }
