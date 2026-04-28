@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.IntArray
import kotlin.Suppress
import spike.factory.DependencyFactory
import spike.factory.DependencyId
import spike.factory.InstructionSetPointer

public class CatEP_Factory() : DependencyFactory() {
  override val maxConstructorArgs: Int = 3

  private val holder0: CatEP_DependencyHolder0 = CatEP_DependencyHolder0(this)

  override val instructionSet: IntArray
    get() = CatEP_InstructionSet.memory

  override fun getInstructionsPointer(id: DependencyId): InstructionSetPointer? = when (id.id) {
    0 -> InstructionSetPointer(0, 8)
    else -> error("Invalid identifier $id")
  }

  override fun instantiate(buffer: Array<Any?>, id: DependencyId): Any = when (id.segment) {
      0 -> holder0.create(buffer, id.position)
      else -> error("Invalid segment")
  }
}
