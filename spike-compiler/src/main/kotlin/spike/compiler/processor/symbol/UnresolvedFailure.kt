package spike.compiler.processor.symbol

import spike.compiler.diagnostic.Diagnostic

class UnresolvedFailure(val causes: List<UnresolvedCompiledType>, val others: List<Diagnostic>)
