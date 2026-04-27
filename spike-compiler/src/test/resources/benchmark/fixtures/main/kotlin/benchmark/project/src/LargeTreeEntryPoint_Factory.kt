package benchmark.project.src

import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.IntArray
import spike.factory.DependencyFactory
import spike.factory.DependencyId
import spike.factory.InstructionSetPointer

public class LargeTreeEntryPoint_Factory() : DependencyFactory() {
  override val maxConstructorArgs: Int = 3

  private val holder0: LargeTreeEntryPoint_DependencyHolder0 =
      LargeTreeEntryPoint_DependencyHolder0(this)

  private val holder1: LargeTreeEntryPoint_DependencyHolder1 =
      LargeTreeEntryPoint_DependencyHolder1(this)

  private val holder2: LargeTreeEntryPoint_DependencyHolder2 =
      LargeTreeEntryPoint_DependencyHolder2(this)

  override val instructionSet: IntArray
    get() = LargeTreeEntryPoint_InstructionSet.memory

  override fun getInstructionsPointer(id: DependencyId): InstructionSetPointer? = when (id.id) {
    0 -> InstructionSetPointer(0, 8_151)
    else -> error("Invalid identifier ${id}")
  }

  override fun instantiate(buffer: Array<Any?>, id: DependencyId): Any = when (id.segment) {
      0 -> holder0.create(buffer, id.position)
      1 -> holder1.create(buffer, id.position)
      2 -> holder2.create(buffer, id.position)
      else -> error("Invalid segment")
  }
}
