package spike.compiler.jsr

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import spike.EntryPoint
import spike.Include
import spike.compiler.conversion.Conversion
import spike.compiler.conversion.ConversionCheck
import spike.compiler.conversion.ProblemReporter
import spike.compiler.conversion.ProblemReporterDistinct
import spike.compiler.conversion.ProblemReporterLogger
import spike.compiler.conversion.SymbolProcessorConversion
import spike.compiler.conversion.SymbolProcessorOnce
import spike.compiler.conversion.SymbolSourceAnnotated

@EntryPoint
interface JsrConversion {
    val processor: SymbolProcessor

    @EntryPoint.Factory
    interface Factory {
        fun create(environment: SymbolProcessorEnvironment): JsrConversion
    }

    companion object
}

@Include
fun jsrProcessor(
    check: ConversionCheck<KSAnnotated>,
    conversion: Conversion<KSAnnotated>,
    reporter: ProblemReporter,
): SymbolProcessor = SymbolProcessorOnce(
    SymbolProcessorConversion(SymbolSourceAnnotated(JsrNames.Inject), check, conversion, reporter),
)

@Include
fun jsrReporter(logger: KSPLogger): ProblemReporter = ProblemReporterDistinct(ProblemReporterLogger(logger))

@Include
fun SymbolProcessorEnvironment.jsrLogger(): KSPLogger = logger

@Include
fun SymbolProcessorEnvironment.jsrCodeGenerator(): CodeGenerator = codeGenerator
