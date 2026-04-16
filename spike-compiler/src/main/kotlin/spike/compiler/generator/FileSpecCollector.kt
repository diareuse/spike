package spike.compiler.generator

import com.squareup.kotlinpoet.FileSpec

fun interface FileSpecCollector {
    fun emit(file: FileSpec)
}