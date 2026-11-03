package spike.compiler.processor

import com.google.devtools.ksp.symbol.FileLocation
import com.google.devtools.ksp.symbol.KSNode
import spike.compiler.diagnostic.Origin
import java.io.File

class OriginNode(val node: KSNode) : Origin {
    override val location: String?
        get() = (node.location as? FileLocation)?.let { "${File(it.filePath).name}:${it.lineNumber}" }

    override fun equals(other: Any?) = other is OriginNode && other.node == node

    override fun hashCode() = node.hashCode()
}

fun KSNode.asOrigin(): Origin = OriginNode(this)
