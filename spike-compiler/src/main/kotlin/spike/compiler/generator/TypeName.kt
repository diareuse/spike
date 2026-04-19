package spike.compiler.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec

inline fun ClassName.peerClass(body: (ClassName) -> String) = peerClass(body(this))
fun ClassName.asRootClass() = ClassName(packageName, simpleName)
fun FileSpec.toClassName() = ClassName(packageName, name)
