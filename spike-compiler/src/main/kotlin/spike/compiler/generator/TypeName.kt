package spike.compiler.generator

import com.squareup.kotlinpoet.ClassName

inline fun ClassName.peerClass(body: (ClassName) -> String) = peerClass(body(this))
fun ClassName.asRootClass() = ClassName(packageName, simpleName)