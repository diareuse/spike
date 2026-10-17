package spike.compiler.jsr

import spike.Include
import spike.compiler.conversion.NameLookup
import spike.compiler.conversion.NameLookupAnnotation
import spike.compiler.conversion.QualifierLookup
import spike.compiler.conversion.QualifierLookupMeta
import spike.compiler.conversion.ScopeLookup
import spike.compiler.conversion.ScopeLookupAnnotation

@Include
fun jsrNames(): NameLookup = NameLookupAnnotation(JsrNames.Named)

@Include
fun jsrScopes(): ScopeLookup = ScopeLookupAnnotation(JsrNames.Singleton, JsrNames.Scope)

@Include
fun jsrQualifiers(): QualifierLookup = QualifierLookupMeta(JsrNames.Qualifier, converted = JsrNames.Named)
