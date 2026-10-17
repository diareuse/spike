package spike.compiler

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.Include

@Include
fun SymbolProcessorEnvironment.logger(): KSPLogger = logger

@Include
fun SymbolProcessorEnvironment.codeGenerator(): CodeGenerator = codeGenerator
