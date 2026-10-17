package spike.compiler.dagger

import spike.Include
import spike.compiler.conversion.NameLookup
import spike.compiler.conversion.NameLookupAnnotation
import spike.compiler.conversion.QualifierLookup
import spike.compiler.conversion.QualifierLookupMeta
import spike.compiler.conversion.ScopeLookup
import spike.compiler.conversion.ScopeLookupAnnotation
import spike.compiler.jsr.JsrNames

@Include
fun daggerNames(): NameLookup = NameLookupAnnotation(JsrNames.Named)

@Include
fun daggerQualifiers(): QualifierLookup = QualifierLookupMeta(JsrNames.Qualifier, converted = JsrNames.Named)

@Include
fun daggerScopes(): ScopeLookup = ScopeLookupAnnotation(DaggerNames.Singleton, JsrNames.Scope)
