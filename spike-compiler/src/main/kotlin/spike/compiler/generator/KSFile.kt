package spike.compiler.generator

import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSNode
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile

fun TypeSpec.Builder.addOriginatingFiles(files: List<KSFile>) = apply {
    for (file in files) addOriginatingKSFile(file)
}

tailrec fun KSNode.findKSFile(): KSFile? {
    return this as? KSFile ?: parent?.findKSFile()
}

fun KSNode.requireKSFile(): KSFile {
    return checkNotNull(findKSFile()) {
        "Cannot find originating file for $this"
    }
}
